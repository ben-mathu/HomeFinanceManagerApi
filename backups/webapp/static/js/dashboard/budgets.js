//let budgetJarList;
//let budgetAmount;
//let btnAddWallet;
//let budgetMonth;
//let budgetYear;
//let accountRef;
//
//let btnTopUpWallet;
//
//function configureBudget() {
//    budgetYear = document.getElementById("budgetYear");
//    budgetMonth = document.getElementById("budgetMonth");
//    budgetAmount = document.getElementById("budgetAmount");
//    budgetJarList = document.getElementById("budgetJarList");
//
//    accountRef = document.getElementById("accountRef");
//    btnTopUpWallet = document.getElementById("topUpWallet");
//
////    btnAddWallet = document.getElementById("btnAddWallet");
////    btnAddWallet.addEventListener("click", function (event) {
////        openBudgetModal();
////    });
//}
//
//function openBudgetModal() {
//    let keys = Object.keys(jars.getJarList());
//
//    let totalBudgetedAmount = 0;
//    keys.forEach(key => {
//        let jarDto = jars.getJar(key);
//        let jar = jarDto.jar;
//        totalBudgetedAmount += jar.amount;
//    });
//
//    // set budget month
//    let date = new Date();
//    let month = date.getMonth() + 1;
//    budgetMonth.innerHTML = month;
//    let year = date.getFullYear();
//    budgetYear.innerHTML = year;
//
//    budgetAmount.innerHTML = totalBudgetedAmount;
//
//    budgetModal.style.display = "block";
//
//    btnTopUpWallet.addEventListener("click", function (event) {
//        addBudgetItem(totalBudgetedAmount);
//    });
//}
//
//function addBudgetItem(amount) {
//    let request = getXmlHttpRequest(); 
//
//    request.onreadystatechange = function() {
//        if (request.readyState == 4) {
//            if (request.status == 200) {
//                let responseData = request.responseText
//                showSuccessNotification(responseData, "");
//            }
//        }
//    }
//
//    let data = serializeBudgetData(amount);
//
//    request.open("POST", ctx + "/dashboard/transactions/send-payment" + data, true);
//    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
//    request.send(data);
//}
//
//function serializeBudgetData(amount) {
//    let data = "";
//
//    let userId = window.localStorage.getItem(userFields.USER_ID);
//    data += userFields.USER_ID + "=" + escape(userId) + "&";
//    let token = window.localStorage.getItem(userFields.TOKEN)
//    data += userFields.TOKEN + "=" + token + "&";
//
//    data += topUpFields.SHORT_CODE + "=" + escape(174379) + "&";
//    data += topUpFields.AMOUNT + "=" + escape(amount) + "&";
//    data += topUpFields.PARTY_A + "=" + escape(accountRef.value) + "&";
//    data += topUpFields.PARTY_B + "=" + escape(174379) + "&";
//    data += topUpFields.PHONE_NUMBER + "=" + escape(accountRef.value) + "&";
//    data += topUpFields.ACCOUNT_REF + "=" + escape("account") + "&";
//    data += topUpFields.TRANSACTION_DESC + "=" + escape("Wallet Top up") + "&";
//    return data;
//}
//
//function showBudgetAmount(budget) {
//    if (budget !== undefined) {
//        if (budget.amount > 0) {
//            budgetAmount.hidden = false;
//            var text = budgetAmount.innerHTML + budget.amount;
////            budgetAmount.innerHTML = text;
//            
////            btnAddWallet.hidden = true;
//        }
//    }
//}