/**
 * Grocery Modal
 */
var groceryName;
var groceryDesc;
var groceryPrice;
var groceryRequired;
var groceryRemaining;
var btnAddGrocery;
let groceriesList;

let groceryTemplate;

/**
 * Grocery table
 */
let groceryListTBody;
var groceryListObj = {};
var grocerySelected;
var isGroceryUpdate = false;

let grocery = {
    grocery_id: "",
    grocery_name: "",
    grocery_description: "",
    grocery_price: "",
    remaining_quantity: "",
    required_quantity: ""
};

let groceryFields = {
    GROCERY_ID: "grocery_id",
    GROCERY_NAME: "grocery_name",
    GROCERY_DESC: "grocery_description",
    GROCERY_PRICE: "grocery_price",
    REMAINING_QT: "remaining_quantity",
    REQUIRED_QT: "required_quantity"
};

function configureGrocery() {
    groceryListTBody = document.getElementById("groceryItems").getElementsByTagName("tbody")[0];
    
    // add table of groceries when modal is opened
    groceryTemplate = document.getElementById("groceryTemplate");
    
    groceriesList = document.getElementById("groceryContainer");
    var btnCloseGroceryModal = document.getElementById("cancelGroceryModal");
    btnCloseGroceryModal.onclick = function() {
        groceryModal.style.display = "none";
    };

    btnAddGrocery = document.getElementById("addItem");
    btnAddGrocery.onclick = function() {
        addGrocery();
    };

    // initialize groceryModal variables
    groceryName = document.getElementById("groceryName");
    groceryDesc = document.getElementById("groceryDesc");
    groceryPrice = document.getElementById("itemPrice");
    groceryRequired = document.getElementById("requireQuantity");
    groceryRemaining = document.getElementById("remainingQuantity");
    
    var btnOpenGroceryModal = document.getElementById("btnOpenGroceryModal");
    btnOpenGroceryModal.onclick = function() {
        groceryModal.style.display = "block";
        groceryDesc.value = "";
        groceryName.value = "";
        groceryPrice.value = "";
        groceryRequired.value = "";
        groceryRemaining.value = "";
    };
}

function setGroceries(groceries) {
    
    if (groceries === undefined) {
        return;
    }
    
    groceryListObj = {};

    groceryListTBody = document.getElementById("groceryItems").getElementsByTagName("tbody")[0];
    groceryListTBody.innerHTML = "";
    
    for (let i = 0; i < groceries.length; i++) {
        groceryListObj[i] = groceries[i];
        
        // get number of rows
        var row = groceryListTBody.insertRow(i);
        if (i%2 === 0 || i === 0) {
            row.style.backgroundColor = "#534c63d2";
            row.style.color = "#FFFFFF";
        }
        row.style.cursor = "pointer";
        
        var indexCell = row.insertCell(0);
        var name = row.insertCell(1);
        var price = row.insertCell(2);
        var quantity = row.insertCell(3);
        var required = row.insertCell(4);
        var cancel = row.insertCell(5);
        
        indexCell.innerHTML = i;
        indexCell.onclick = (function() {
            return function() {
                onItemClick(i);
            };
        })();
        
        name.innerHTML = groceries[i].grocery_name;
        name.onclick = (function() {
            return function() {
                onItemClick(i);
            };
        })();
        
        price.innerHTML = groceries[i].grocery_price;
        price.onclick = (function() {
            return function() {
                onItemClick(i);
            };
        })();
        
        quantity.innerHTML = groceries[i].remaining_quantity;
        quantity.onclick = (function() {
            return function() {
                onItemClick(i);
            };
        })();
        
        required.innerHTML = groceries[i].required_quantity;
        required.onclick = (function() {
            return function() {
                onItemClick(i);
            };
        })();
        
        cancel.innerHTML = "x";
        cancel.onclick = (function() {
            return function() {
                onItemRemoved(i);
            };
        })();
    }
}

function setGroceriesForNotification(groceries, body) {
    
    if (groceries === undefined) {
        return;
    }

    groceryListTBody = body.getElementsByTagName("tbody")[0];
    
    for (let i = 0; i < groceries.length; i++) {
        // get number of rows
        var row = groceryListTBody.insertRow(i);
        if (i%2 === 0 || i === 0) {
            row.style.backgroundColor = "#534c63d2";
            row.style.color = "#FFFFFF";
        }
        row.style.cursor = "pointer";
        
        var name = row.insertCell(0);
        var price = row.insertCell(1);
        var quantity = row.insertCell(2);
        var required = row.insertCell(3);
        
        name.innerHTML = groceries[i].grocery_name;
        price.innerHTML = groceries[i].grocery_price;
        quantity.innerHTML = groceries[i].remaining_quantity;
        required.innerHTML = groceries[i].required_quantity;

        groceryListObj[groceries[i].grocery_id] = groceries[i];
    }
}

/**
 * shows error for grocery modal
 */
function showError() {
    var error = document.getElementById("jarModalError");
    error.innerHTML = "An Error occured please try again.";
    error.hidden = false;
}

/**
 * adds grocery values to grocery modal
 */
function addGrocery() {
    groceryListTBody = document.getElementById("groceryItems").getElementsByTagName("tbody")[0];

    var name = groceryName.value;
    var desc = groceryDesc.value;
    var price = (parseFloat(groceryPrice.value) * parseFloat(groceryRequired.value)).toString();
    var required = groceryRequired.value;
    var remaining = groceryRemaining.value;

    grocery = new Object();
    grocery.grocery_name = name;
    grocery.grocery_description = desc;
    grocery.grocery_price = price;
    grocery.remaining_quantity = remaining;
    grocery.required_quantity = required;
    
    var index = Object.keys(groceryListObj).length;
    groceryListObj[index] = grocery;

    // get number of rows
    var row;
    row = groceryListTBody.insertRow(index);
    var indexCell = row.insertCell(0);
    var nameCell = row.insertCell(1);
    var priceCell = row.insertCell(2);
    var descCell = row.insertCell(3);
    var quantityCell = row.insertCell(4);
    var requiredCell = row.insertCell(5);
    var cancel = row.insertCell(6);

    indexCell.innerHTML = index;
    nameCell.innerHTML = name;
    priceCell.innerHTML = price;
    descCell.innerHTML = desc;
    quantityCell.innerHTML = remaining;
    requiredCell.innerHTML = required;
    cancel.innerHTML = "x";

    onRowLoaded(price);

    let obj = groceryListObj[index];

    indexCell.onclick = (function() {
        return function() {
            onItemClick(index);
        };
    })();
    
    nameCell.onclick = (function() {
        return function() {
            onItemClick(index);
        };
    })();
    
    priceCell.aonclick = (function() {
        return function() {
            onItemClick(index);
        };
    })();
    
    descCell.onclick = (function() {
        return function() {
            onItemClick(index);
        };
    })();
    
    quantityCell.onclick = (function() {
        return function() {
            onItemClick(index);
        };
    })();
    
    requiredCell.onclick = (function() {
        return function() {
            onItemClick(index);
        };
    })();

    cancel.onclick = (function () {
        return function () {
            onItemRemoved(index);
        };
    })();

    if (index%2 === 0) {
        row.style.backgroundColor = "#534c63";
        row.style.color = "#F8FFE1";
    }
    row.style.cursor = "pointer";

    groceryModal.style.display = "none";

    grocerySelected = null;
}

/**
 * update the entry in grocery list
 * @param {integer} index position of the edited item in list
 */
function updateGrocery(index) {
    // updates a row when user edit an item in the list
    
    groceryListTBody = document.getElementById("groceryItems").getElementsByTagName("tbody")[0];
    let row = groceryListTBody.rows[index];
    var cells = row.cells;
    
    var name = groceryName.value;
    var desc = groceryDesc.value;
    var price = (parseFloat(groceryPrice.value) * parseFloat(groceryRequired.value)).toString();
    var required = groceryRequired.value;
    var remaining = groceryRemaining.value;

    var previousAmount = cells[2].innerHTML;
    cells[1].innerHTML = name;
    cells[2].innerHTML = price;
    cells[3].innerHTML = desc;
    cells[4].innerHTML = remaining;
    cells[5].innerHTML = required;
    
    updateTotalAmount(previousAmount, price);

//    let templateClone = document.importNode(template.content, true);
//
//    groceriesList.innerHTML = "";
//    groceriesList.appendChild(groceryTemplate);
//    groceriesList.appendChild(templateClone);
}

function getGroceryItemIndex(sample) {
    var count = 0;
    var keys = Object.keys(groceryListObj);
    for (let i = 0; i < keys.length; i++) {
        var key = keys[i];
        if (sample === key) {
            break;
        } else {
            count++;
        }
    }

    return count;
}

/**
 * defines a function triggered when the user selects an item in the grocery list.
 * @param {integer} row defines the position of the selected row
 */
function onItemClick(row) {
    groceryModal.style.display = "block";
    
    grocerySelected = groceryListObj[row];
    var name = grocerySelected.grocery_name;
    var desc = grocerySelected.grocery_description;
    var price = grocerySelected.grocery_price;
    var required = grocerySelected.required_quantity;
    var remaining = grocerySelected.remaining_quantity;

    groceryName.value = name;
    groceryDesc.value = desc;
    groceryPrice.value = price;
    groceryRequired.value = required;
    groceryRemaining.value = remaining;

    isGroceryUpdate = true;
    
    btnAddGrocery.onclick = function() {
        if (!validGroceryInput()) {
            return;
        }
        updateGrocery(row);
    };
}

function validGroceryInput() {
    
}

/**
 * remove item on grocery list
 * 
 * @param {string} groceryId id of the item being removed
 */
function onItemRemoved(rowIndex) {
    groceryListTBody = document.querySelector("#groceryItems").getElementsByTagName("tbody")[0];
    
    let rows = groceryListTBody.rows;
    for (let i = 0; i < rows.length; i++) {
        let row = rows[i];
        let indexCell = row.cells[0];
        let index = indexCell.innerHTML;
        
        if (parseInt(index) === rowIndex) {
            groceryListTBody.deleteRow(i);
            let amount = groceryListObj[rowIndex].grocery_price;
            onRowRemoved(amount);
            
            deleteGroceries(groceryListObj[rowIndex]);
            delete groceryListObj[rowIndex];
            break;
        }
    }
}

/**
 * Delete grocery from database
 * @param {Grocery} grocery
 */
function deleteGroceries(grocery) {
    let request = getXmlHttpRequest();
    
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            if (request.status === 200) {
                getAllMoneyJars();
            }
        }
    };
    
    let groceryId = grocery.grocery_id;
    let token = window.localStorage.getItem(userFields.TOKEN);
    let data = groceryFields.GROCERY_ID + "=" + groceryId;
    
    request.open("DELETE", ctx + "/api/groceries/delete-grocery?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.setRequestHeader(requestHeader.AUTHORIZATION, "Bearer " + token);
    request.send();
}