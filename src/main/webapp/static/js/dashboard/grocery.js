/**
 * Grocery Modal
 */
var groceryName;
var groceryDesc;
var groceryPrice;
var groceryRequired;
var groceryRemaining;
var btnAddGrocery;

/**
 * Grocery table
 */
var groceryListTBody;
var groceryListObj = {};
var grocerySelected;
var isGroceryUpdate = false;

function configureGrocery() {
    groceryListTBody = document.getElementById("groceryItems").getElementsByTagName("tbody")[0];

    var btnCloseGroceryModal = document.getElementById("cancelGroceryModal");
    btnCloseGroceryModal.onclick = function() {
        groceryModal.style.display = "none";
    }

    btnAddGrocery = document.getElementById("addItem");
    btnAddGrocery.onclick = function() {
        sendRequestGrocery();
    }

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
    }
}

/**
 * 
 * @param {Callback} modalDetails Callback when window first loads
 */
function openGroceryModal(callback) {
    groceryModal.style.display = "block";
    groceryDesc.value = "";
    groceryName.value = "";
    groceryPrice.value = "";
    groceryRequired.value = "";
    groceryRemaining.value = "";

    btnAddGrocery.onclick = function() {
        updateGrocery(callback);
    }
}

function updateGrocery(callback) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                setGrocery(JSON.parse(json));

                delete incomplete[callback.key];

                let len = Object.keys(incomplete).length;

                if (len > 1) {
                    callback.onNext(callback);
                } else if(len == 1) {
                    callback.onDone(callback);
                } else {
                    callback.onComplete();
                }
            } else {
                showError();
            }
        }
    }

    var data = serializeData();

    request.open("PUT", ctx + "/dashboard/groceries-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

function showError() {
    var error = document.getElementById("grocery-groceryModal-error");
    error.innerHTML = "An Error occured please try again.";
    error.hidden = false;
}

function setGrocery(grocery) {
    groceryListObj[grocery.grocery_id] = grocery;


    var item = groceryListObj[grocery.grocery_id];
    // get index of new/old entry
    var index = getGroceryItemIndex(item.grocery_id);

    // get number of rows
    var row;

    if (isGroceryUpdate) {
        row = groceryListTBody.rows[index];
        var cells = row.cells;
        cells[0].innerHTML = item.grocery_name;
        cells[1].innerHTML = item.grocery_price;
        cells[2].innerHTML = item.remaining_quantity;
        cells[3].innerHTML = item.required_quantity;
        isGroceryUpdate = false;
    } else {
        row = groceryListTBody.insertRow(index);
        var name = row.insertCell(0);
        var price = row.insertCell(1);
        var quantity = row.insertCell(2);
        var required = row.insertCell(3);

        name.innerHTML = item.grocery_name;
        price.innerHTML = item.grocery_price;
        quantity.innerHTML = item.remaining_quantity;
        required.innerHTML = item.required_quantity;
    }

    row.onclick = (function() {
        return function() {
            onItemClick(obj.grocery_id);
        }
    })();

    if (index%2 == 0) {
        row.style.backgroundColor = "#534c63d2";
    }
    row.style.cursor = "pointer";

    groceryModal.style.display = "none";

    grocerySelected = null;
}

/**
 * sends a request throught the controller to add an item to grocery
 */
function sendRequestGrocery() {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                setGrocery(JSON.parse(json));
            } else {
                showError();
            }
        }
    }

    var data = serializeData();

    request.open("PUT", ctx + "/dashboard/groceries-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

function serializeData() {
    var userId = "user_id=" + escape(window.localStorage.getItem("user_id"));
    var token = "token=" + escape(window.localStorage.getItem("token"));
    var username = "username=" + escape(window.localStorage.getItem("username"));

    // serialize grocery items details
    var id = grocerySelected == null ? "" : grocerySelected.grocery_id;
    var name = groceryName.value;
    var desc = groceryDesc.value;
    var price = groceryPrice.value;
    var required = groceryRequired.value;
    var remaining = groceryRemaining.value;

    if (name == "" || desc == "" || price == "" || required == "" || remaining == "") {

        var error = document.getElementById("grocery-groceryModal-error");
        error.innerHTML = "All fields are required!";
        error.hidden = false;
        return;
    }

    id = "grocery_id=" + escape(id);
    remaining = "remaining_quantity=" + escape(remaining);
    required = "required_quantity=" + escape(required);
    price = "grocery_price=" + escape(price);
    desc = "grocery_description=" + escape(desc);
    name = "grocery_name=" + escape(name);

    var data = userId + "&";
    data += token + "&";
    data += username + "&";
    data += name + "&";
    data += desc + "&";
    data += price + "&";
    data += required + "&";
    data += remaining + "&";
    data += id;

    return data;
}

function getGroceryItemIndex(sample) {
    var count = 0;
    var keys = Object.keys(groceryListObj);
    for (let i = 0; i < keys.length; i++) {
        var key = keys[i];
        if (sample == key) {
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