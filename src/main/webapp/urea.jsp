<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%
    // Auto values from servlet
    String reportDate = (String) request.getAttribute("reportdate");
Double totalUrea = (Double) request.getAttribute("totalureaproduce");
Double ureaOpening = (Double) request.getAttribute("ureaopeningstock");
Double bagOpening = (Double) request.getAttribute("bagopeningstock");
Double neemBagOpening = (Double) request.getAttribute("neembagopeningstock");
Double neemOilOpening = (Double) request.getAttribute("neemoilopeningstock");

    String error = (String) request.getAttribute("error");

    if (reportDate == null) {
        reportDate = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
%>

<!DOCTYPE html>
<html>
<head>
<title>Urea Daily Report</title>

<style>
body {
    font-family: Arial;
    background:#eaeaea;
}
.header {
    background:#b7d8ea;
    padding:6px;
    font-weight:bold;
}
h3 {
    color:#800000;
    text-align:center;
}
table {
    width:70%;
    margin:auto;
}
td {
    padding:6px;
}
input[type=text] {
    width:90px;
}
.footer {
    text-align:center;
    padding:15px;
}
button {
    padding:4px 20px;
}
</style>

<script>
/* ================= CONSTANTS ================= */
var ureadaytarget = 1550;
var ureayeartarget = 511500;

/* ================= ON LOAD ================= */
function setForm() {
    disable("reportdate");
    disable("ureaopeningstock");
    disable("ureaclosingstock");
    disable("bagopeningstock");
    disable("bagclosingstock");
    disable("neembagopeningstock");
    disable("neembagclosingstock");
    disable("neemoilopeningstock");
    disable("neemoilclosingstock");

    var err = document.getElementById("error").value;
    if (err === "N") {
        alert("You are not authorized");
        disableAll(true);
    }
}

function disable(id){
    var e=document.getElementById(id);
    if(e) e.disabled=true;
}

function disableAll(flag){
    document.querySelectorAll("input").forEach(i=>i.disabled=flag);
}

/* ================= VALIDATION ================= */
function num(id){
    var v=document.getElementById(id).value;
    return v===""?0:parseFloat(v);
}

function checkno(el){
    if(el.value!=="" && isNaN(el.value)){
        alert("Only numbers allowed");
        el.value="";
        el.focus();
    }
}

/* ================= CALCULATIONS ================= */
function calcUreaClose(){
    document.getElementById("ureaclosingstock").value =
        (num("ureaproduction")+num("ureaopeningstock")
        -num("rail")-num("road")-num("plainureatogoldurea")).toFixed(3);
}

function calcBagClose(){
    document.getElementById("bagclosingstock").value =
        num("bagopeningstock")+num("receipt")-num("consumption");
}

function calcNeemBagClose(){
    document.getElementById("neembagclosingstock").value =
        num("neembagopeningstock")+num("neemreceipt")-num("neemconsumption");
}

function calcNeemOilClose(){
    document.getElementById("neemoilclosingstock").value =
        (num("neemoilopeningstock")+num("neemoilreceipt")
        -num("neemoilconsumption")).toFixed(5);
}

/* ================= SAVE ================= */
function saveForm(){
    document.getElementById("jmethod").value="saveRecords";
    document.forms[0].submit();
}
</script>

</head>

<body onload="setForm()">

<div class="header">
    Department : Information Technology
    <span style="float:right">User : Admin &nbsp;&nbsp; <%=reportDate%></span>
</div>

<h3>Urea Daily Report Data Entry Form</h3>

<form method="post" action="urea">

<table>
<tr>
<td>Report Date</td>
<td><input id="reportdate" name="reportdate" value="<%=reportDate%>"></td>
<td>Total Urea Produce</td>
<td><input value="<%=totalUrea%>" disabled></td>
</tr>

<tr>
<td>Urea Production</td>
<td><input id="ureaproduction" name="ureaproduction" onblur="checkno(this)"></td>
<td>Neem Urea Production</td>
<td><input id="neemureaproduction" name="neemureaproduction"></td>
<td>Plain → Gold</td>
<td><input id="plainureatogoldurea" name="plainureatogoldurea"></td>
</tr>
</table>

<h3>Production Loss</h3>
<table>
<tr>
<td>Raw Materials</td><td><input id="rawmaterials"></td>
<td>Export Power</td><td><input id="exportpower"></td>
<td>Mechanical</td><td><input id="mechanical"></td>
<td>Electrical</td><td><input id="electrical"></td>
</tr>
</table>

<h3>Urea Despatch</h3>
<table>
<tr>
<td>Rail</td><td><input id="rail"></td>
<td>Road</td><td><input id="road" onblur="calcUreaClose()"></td>
<td>Opening Stock</td><td><input id="ureaopeningstock" value="<%=ureaOpening%>"></td>
<td>Closing Stock</td><td><input id="ureaclosingstock"></td>
</tr>
</table>

<h3>HDPE Bags</h3>
<table>
<tr>
<td>Opening</td><td><input id="bagopeningstock" value="<%=bagOpening%>"></td>
<td>Receipt</td><td><input id="receipt"></td>
<td>Consumption</td><td><input id="consumption" onblur="calcBagClose()"></td>
<td>Closing</td><td><input id="bagclosingstock"></td>
</tr>
</table>

<h3>Neem Bags</h3>
<table>
<tr>
<td>Opening</td><td><input id="neembagopeningstock" value="<%=neemBagOpening%>"></td>
<td>Receipt</td><td><input id="neemreceipt"></td>
<td>Consumption</td><td><input id="neemconsumption" onblur="calcNeemBagClose()"></td>
<td>Closing</td><td><input id="neembagclosingstock"></td>
</tr>
</table>

<h3>Neem Oil</h3>
<table>
<tr>
<td>Opening</td><td><input id="neemoilopeningstock" value="<%=neemOilOpening%>"></td>
<td>Receipt</td><td><input id="neemoilreceipt"></td>
<td>Consumption</td><td><input id="neemoilconsumption" onblur="calcNeemOilClose()"></td>
<td>Closing</td><td><input id="neemoilclosingstock"></td>
</tr>
</table>

<input type="hidden" id="jmethod" name="jmethod">
<input type="hidden" id="error" value="<%=error%>">

<div class="footer">
<button type="button" onclick="saveForm()">Save</button>
<button type="button" onclick="window.close()">Exit</button>
</div>

</form>
</body>
</html>
