let transactionTBody;

let transactions = {
    transactionList: {},
    onAddTransactionListener: function(index, transaction){},
    onRemoveTransactionListener: function(index){},
    addTransaction: function(index, val) {
        this.transactionList[index] = val;
        this.onAddTransactionListener(index, val);
    },
    removeTransaction: function(index) {
        delete this.transactionList[index];
        this.onRemoveTransactionListener(index);
    },
    getTransaction: function(index) {
        return this.transactionList[index];
    },
    getAll: function () {
        return this.transactionList;
    },
    registerAddListener: function (listener) {
        this.onAddTransactionListener = listener;
    },
    registerRemoveListener: function (listener) {
        this.onRemoveTransactionListener = listener;
    }
};
/**
 * Encapsulates the settings for transactions requirements
 */
function configureTransactions() {
    let chartContainer = document.getElementById("chartContainer");
    
    transactionCanvas = document.getElementById("lineGraphCanvas");
    
    let width = window.innerWidth;
    let chartContainerWidth = 75 / 100 * width / 2;
    
    transactionCanvas.width = chartContainerWidth;
    transactionCanvas.height = 250;
    
    window.addEventListener("resize", function (event) {
        width = event.target.innerWidth;
        chartContainerWidth = 75 / 100 * width / 2;
        transactionCanvas.width = chartContainerWidth;
        transactionCanvas.height = 250;
        
        let properties = {
            canvas: transactionCanvas,
            transactions: Object.values(transactions.getAll()),
            colors: ["#008FB4"]
        };

        let lineGraph = new LineGraph(properties);
        lineGraph.draw();
    });
    
    transactionTBody = document.getElementById("transactionTable").getElementsByTagName("tbody")[0];
    
    transactions.registerAddListener(function (index, transaction) {
        let row = transactionTBody.insertRow(index);
        
        let indexCol = row.insertCell(0);
        let descriptionCol = row.insertCell(1);
        let amountCol = row.insertCell(2);
        let statusCol = row.insertCell(3);
        let timestampCol = row.insertCell(4);
        
        indexCol.innerHTML = index;
        descriptionCol.innerHTML = transaction.transaction_desc;
        amountCol.innerHTML = transaction.amount;
        if (transaction.payment_status) {
            statusCol.innerHTML = "Paid";
        }
        timestampCol.innerHTML = transaction.payment_timestamp;
        
        if (index%2 === 0) {
            row.style.backgroundColor = "#534c63d2";
        }
        
        row.style.cursor = "pointer";
    });
    
    transactions.registerRemoveListener(function (val) {
        // remove transaction from table
    });
    
//    getAllTransactions();
}

function getAllTransactions() {
    let request = getXmlHttpRequest();
    
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            if (request.status === 200) {
                let jsonStr = request.responseText;
                let transactionsList = JSON.parse(jsonStr).transactions;
                
                let count = 0;
                transactionsList.forEach(transaction => {
                    transactions.addTransaction(count, transaction);
                    count++;
                });
                
                let properties = {
                    canvas: transactionCanvas,
                    transactions: Object.values(transactions.getAll()),
                    colors: ["#008FB4"]
                };

                let lineGraph = new LineGraph(properties);
                lineGraph.draw();
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }
        }
    };
    
    let data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID)) + "&";
    data += userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN));
    
    request.open("GET", ctx + "/dashboard/transactions/get-transactions?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

let getMaxAmount = function(sortedList) {
    let keys = Object.keys(sortedList);
    if (keys.length < 2) {
        return sortedList[0];
    } else {
        let temp = 0;
        let keys = Object.keys(sortedList);
        keys.forEach(key => {
            if (temp < sortedList[key]) {
                temp = sortedList[key];
            }
        });

        this.actualMaxAmount = temp;
        return temp;
    }
};

let getScale = function (maxAmount) {
    let value = maxAmount;
    if (value > 9999999 && value < 1000000000) {
        return 100000;
    } else if (value > 999999 && value < 100000000) {
        return 10000;
    } else if (value > 99999 && value < 10000000) {
        return 1000;
    } else if (value > 9999 && value < 100000) {
        return 100;
    } else if (value > 999 && value < 10000) {
        return 10;
    } else if (value > 99 && value < 1000) {
        return 1;
    } else if (value > 9 && value < 100) {
        return 1;
    } else if (value > 1) {
        return 1;
    }
};

let sortedTransactionByDate = function (list) {
    for (var i = 0; i < list.length; i++) {
        if (i === list.length - 1) {
            break;
        }

        let curr = list[i];
        let next = list[i + 1];

        let currDate = new Date(curr.payment_timestamp);
        let nextDate = new Date(next.payment_timestamp);
        if (currDate.getDate() > nextDate.getDate()) {
            let temp = next;
            list[i + 1] = list[i];
            list[i] = temp;
            i = 0;
        }
    }
    
    // get total amount
    let monthlyAmount = {};
    list.forEach(transaction => {
        let date = new Date(transaction.payment_timestamp);
        let monIndex = date.getMonth() + 1;

        let prevAmount = monthlyAmount[monIndex] === undefined ? 0 : monthlyAmount[monIndex];
        monthlyAmount[monIndex] = transaction.amount + prevAmount;
    });
    
    return monthlyAmount;
};


