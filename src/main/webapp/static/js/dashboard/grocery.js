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
}

function configureGrocery() {
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

    let groceryTemplate = document.getElementById("groceryTemplate");

    var body = groceryTemplate.content.querySelector("#groceryItems").getElementsByTagName("tbody")[0];
    
    for (let i = 0; i < groceries.length; i++) {
        // get number of rows
        var row = body.insertRow(i);
        if (i%2 === 0 || i === 0) {
            row.style.backgroundColor = "#534c63d2";
        }
        row.style.cursor = "pointer";

        row.onclick = (function() {
            var currentIndex = i;
            return function() {
                onItemClick(groceries[currentIndex].grocery_id);
            };
        })();
        
        var name = row.insertCell(0);
        var price = row.insertCell(1);
        var quantity = row.insertCell(2);
        var required = row.insertCell(3);
        
        name.innerHTML = groceries[i].grocery_name;
        price.innerHTML = groceries[i].grocery_price;
        quantity.innerHTML = groceries[i].remaining_quantity;
        required.innerHTML = groceries[i].required_quantity;

        groceryListObj[groceries[i].grocery_id] = items[i];
    }

    let templateClone = document.importNode(groceryTemplate.content, true);
    return templateClone;
}

/**
 * 
 * @param {Callback} modalDetails Callback when window first loads
 */
// function openGroceryModal() {
//     groceryModal.style.display = "block";
//     groceryDesc.value = "";
//     groceryName.value = "";
//     groceryPrice.value = "";
//     groceryRequired.value = "";
//     groceryRemaining.value = "";

//     btnAddGrocery.onclick = function() {
//         addGrocery();
//     }
// }

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
    let template = document.getElementById("groceryTemplate");
    groceryListTBody = template.content.querySelector("groceryItems").getElementsByTagName("tbody")[0];

    var name = groceryName.value;
    var desc = groceryDesc.value;
    var price = groceryPrice.value;
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
    var nameCell = row.insertCell(0);
    var priceCell = row.insertCell(1);
    var descCell = row.insertCell(2);
    var quantityCell = row.insertCell(3);
    var requiredCell = row.insertCell(4);
    var cancel = row.insertCell(5);

    nameCell.innerHTML = name;
    priceCell.innerHTML = price;
    descCell.innerHTML = desc;
    quantityCell.innerHTML = remaining;
    requiredCell.innerHTML = required;
    cancel.innerHTML = "x";

    onRowLoaded(price);

    let obj = groceryListObj[index];

    row.onclick = (function() {
        return function() {
            onItemClick(obj.grocery_id);
        };
    })();

    cancel.onclick = (function () {
        return function () {
            onItemRemoved(obj.grocery_id);
        };
    })();

    if (index%2 === 0) {
        row.style.backgroundColor = "#534c63d2";
    }
    row.style.cursor = "pointer";

    groceryModal.style.display = "none";

    grocerySelected = null;

    let templateClone = document.importNode(template.content, true);

    groceriesList.innerHTML = "";
    groceriesList.appendChild(templateClone);
}

/**
 * update the entry in grocery list
 * @param {integer} index position of the edited item in list
 */
function updateGrocery(index) {
    // updates a row when user edit an item in the list
    let groceriesList = document.getElementById("groceryContainer");
    let template = document.getElementById("groceryTemplate");
    groceryListTBody = template.content.querySelector("groceryItems").getElementsByTagName("tbody")[0];
    let row = groceryListTBody.rows[index];
    var cells = row.cells;

    var previousAmount = cells[1].innerHTML;
    cells[0].innerHTML = name;
    cells[1].innerHTML = price;
    cells[2].innerHTML = desc;
    cells[3].innerHTML = remaining;
    cells[4].innerHTML = required;
    
    updateTotalAmount(previousAmount, price);

    let templateClone = document.importNode(template.content, true);

    groceriesList.innerHTML = "";
    groceriesList.appendChild(templateClone);
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
}

/**
 * remove item on grocery list
 * 
 * @param {string} groceryId id of the item being removed
 */
function onItemRemoved(groceryId) {
    
}