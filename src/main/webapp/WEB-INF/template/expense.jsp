<template id="expenseTemplate">
    <div class="expense-details-container">
        <table>
            <thead>
                <tr>
                    <th>Expense Attribute</th>
                    <th>Expense Value</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Name</td>
                    <td><span id="expName" class="normal-font"></span></td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td><span id="expDesc" class="normal-font"></span></td>
                </tr>
                <tr>
                    <td>Amount</td>
                    <td><span id="expAmount" class="normal-font"></span></td>
                </tr>
                <tr>
                    <td>Payee</td>
                    <td><span id="expPayeeName" class="normal-font"></span></td>
                </tr>
                <tr>
                    <td>Business No.</td>
                    <td><span id="expBusinessNumber" class="normal-font"></span></td>
                </tr>
                <tr>
                    <td>Account No.</td>
                    <td><span id="accountNumber" class="normal-font"></span></td>
                </tr>
            </tbody>
        </table>
    </div>
</template>