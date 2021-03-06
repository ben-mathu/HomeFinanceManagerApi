<template id="paymentTemplate">
    <div id="paymentDetailsContainer" class="dialog-content">
        <input id="moneyJarId" type="text" name="money-jar-id" placeholder="Jar ID" value="" hidden>
        <strong>Payment for:<span id="nameTitle"></span></strong>
        <div class="payment-body" >
            <span class="dialog-subtitle">Expenditure Type:&nbsp;<span id="paymentCategorySpan"></span>&nbsp;<span style="font-size:21px">KSH. <span id="paymentAmount"></span></span>&nbsp;<span id="expandButton" class="expand-button" >Expand</span></span>
        </div>
        <div id="liabilitySection" class="liability-section" hidden>
            <div id="paymentGrocery">
                <table id="paymentGroceryItems">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Description</th>
                            <th>Remaining</th>
                            <th>Required</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
            <div id="paymentExpense">
                <%@ include file = "expense.jsp" %>
            </div>
        </div>
        
        <div>
            <input id="btnEditDetails" class="btn2" type="button" name="editFields" value="Edit">
            <input id="btnPay" class="btn2" type="button" name="Accept" value="Accept">
        </div>
    </div>
</template>