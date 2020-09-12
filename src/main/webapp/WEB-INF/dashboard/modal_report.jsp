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
                        <td>Three Month Ending</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td id="reportdate"><%= new SimpleDateFormat("MMM dd, yyyy").format(new Date()) %></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><strong><%= new SimpleDateFormat("yyyy").format(new Date()) %></strong></td>
                    </tr>
                </thead>
                <tbody style="text-align: right;">
                    <tr>
                        <td class="td-report-left">Revenue/Income</td>
                    </tr>
                    <tr>
                        <td class="td-report-center">Income</td>
                        <td id="income">0</td>
                    </tr>
                    <tr>
                        <td class="td-report-center">Total</td>
                        <td id="incomeTotal">0</td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td class="td-report-left">Cost and deductions</td>
                    </tr>
                    <tr id="exepenses">
                        <td class="td-report-center">Total cost and deductions</td>
                        <td id="expenseTotal">0</td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td class="td-report-left">Income before tax</td>
                        <td>0</td>
                    </tr>
                    <tr>
                        <td class="td-report-center">Tax</td>
                        <td>0</td>
                    </tr>
                    <tr>
                        <td class="td-report-center">Income after tax</td>
                        <td>0</td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td class="td-report-left">Net income</td>
                        <td>0</td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
            <canvas id="reportCanvas"></canvas>
            <legend for="reportCanvas"></legend>
        </div>
        <div>
            <input id="btnDownloadPdf" class="btn2 float-right" type="button" value="Download"/>
        </div>
    </div>
</div>
