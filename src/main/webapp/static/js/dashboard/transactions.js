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
}
/**
 * Encapsulates the settings for transactions requirements
 */
function configureTransactions() {
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
        statusCol.innderHTML = transaction.payment_status;
        timestampCol.innerHTML = transaction.payment_timestamp;
        
        if (index%2 === 0) {
            row.style.backgroundColor = "#534c63d2";
        }
        
        row.style.cursor = "pointer";
    });
    
    transactions.registerRemoveListener(function (val) {
        // remove transaction from table
    });
    
    getAllTransactions();
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
            }
        }
    };
    
    let data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID)) + "&";
    data += userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN));
    
    request.open("GET", ctx + "/dashboard/transactions/get-transactions?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}


