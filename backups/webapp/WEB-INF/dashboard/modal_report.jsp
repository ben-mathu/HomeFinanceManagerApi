<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<div id="reportModal" class="modal">
    <div class="modal-content report-modal-container">
        <div id="formulae">
            <strong>Financial Report: House of Mathu</strong>
            <table style="width: 100%">
                <thead>
                    <tr>
                        <td></td>
                        <td id="numMonths">Three Month Ending</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td id="reportdate"><%= new SimpleDateFormat("MMM dd, yyyy").format(new Date()) %></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td id="reportYear"><strong><%= new SimpleDateFormat("yyyy").format(new Date()) %></strong></td>
                    </tr>
                </thead>
                <tbody style="text-align: right;">
                    <tr>
                        <td class="td-report-left"><strong>Revenue/Income</strong></td>
                    </tr>
                    <tr>
                        <td class="td-report-left">Income</td>
                        <td id="reportIncome" class="td-report-left">0</td>
                    </tr>
                    <tr>
                        <td class="td-report-left">Total</td>
                        <td id="incomeTotal" class="td-report-left">0</td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td> </td>
                    </tr>
                </tbody>
                <tbody id="expenseBody">
                    <tr>
                        <td class="td-report-left"><strong>Cost and deductions</strong></td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td> </td>
                    </tr>
                </tbody>
<!--                <tbody>
                    <tr>
                        <td class="td-report-left"><strong>Income before tax</strong></td>
                        <td id="incomeBeforeTax" class="td-report-left">0</td>
                    </tr>
                    <tr>
                        <td class="td-report-left">Tax</td>
                        <td id="incomeTax" class="td-report-left">0</td>
                    </tr>
                    <tr>
                        <td class="td-report-left">Personal Relief</td>
                        <td id="personalRelief" class="td-report-left">0</td>
                    </tr>
                    <tr>
                        <td class="td-report-left"><strong>Income after tax</strong></td>
                        <td id="incomeAfterTax" class="td-report-left">0</td>
                    </tr>
                </tbody>-->
                <tbody>
                    <tr>
                        <td> </td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td class="td-report-left"><strong>Net income</strong></td>
                        <td id="netIncome" class="td-report-left">0</td>
                    </tr>
                </tbody>
            </table>
            <canvas id="reportCanvas"></canvas>
            <legend for="reportCanvas"></legend>
        </div>
    </div>
</div>
