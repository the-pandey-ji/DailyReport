<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%
    // Auto values from servlet
    String reportDate = (String) request.getAttribute("reportdate");
Double totalUrea = (Double) request.getAttribute("totalureaproduce");
System.out.println("Total Urea Produce: " + totalUrea);
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
function calcNeemDespatch(){
    document.getElementById("neemureadespatch").value =
        (num("rail")+num("road")).toFixed(2);
     
}

/* ================= SAVE ================= */
function saveForm(){
    document.getElementById("saverecord").value="saveRecords";
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
<td><input value="<%=totalUrea%>" readonly></td>
</tr>

<tr>
<td>Urea Production</td>
<td><input id="ureaproduction" name="ureaproduction" onblur="checkno(this); calcUreaClose()"></td>
<td>Neem Urea Production</td>
<td><input id="neemureaproduction" name="neemureaproduction" onblur="checkno(this)"></td>
<td>Plain Transfer to Gold</td>
<td><input id="plainureatogoldurea" name="plainureatogoldurea" onblur="calcUreaClose()"></td>
</tr>

<tr>
<td>CO2 Consumption</td>
<td><input id="co2consumption" name="co2consumption" onblur="checkno(this)"></td>
<td>Steam Consumption</td>
<td><input id="steamconsumption" name="steamconsumption"></td>
<td>Steam Hours</td>
<td><input id="steamhours" name="steamhours" ></td>
</tr>
</table>

<h3>Production Loss</h3>
<table>
<tr>
<td>Raw Materials</td>
<td><input id="rawmaterials" name="rawmaterials" onblur="checkno(this)"></td>

<td>Export Power</td>
<td><input id="exportpower" name="exportpower" onblur="checkno(this)"></td>

<td>Mechanical</td>
<td><input id="mechanical" name="mechanical" onblur="checkno(this)"></td>

<td>Electrical</td>
<td><input id="electrical" name="electrical" onblur="checkno(this)"></td>
</tr>

<tr>
<td>Instrumentation</td>
<td><input id="instrumentation" name="instrumentation" onblur="checkno(this)"></td>

<td>Process</td>
<td><input id="process1" name="process1" onblur="checkno(this)"></td>

<td>Shutdown</td>
<td><input id="shutdown" name="shutdown" onblur="checkno(this)"></td>


<td>Others</td>
<td><input id="others" name="others" onblur="checkno(this)"></td>

</tr>

<tr>
<td>Annual Shutdown</td>
<td><input id="annualshutdown" name="annualshutdown" onblur="checkno(this)"></td>
<td colspan="6"></td>
</tr>
</table>


<h3>Urea Despatch</h3>
<table>
<tr>
<td>Rail</td><td><input id="rail" onblur="calcUreaClose();calcNeemDespatch()"></td>
<td>Road</td><td><input id="road" onblur="calcUreaClose();calcNeemDespatch()"></td>
<td>Opening Stock</td><td><input id="ureaopeningstock" value="<%=ureaOpening%>"></td>
<td>Closing Stock</td><td><input id="ureaclosingstock"></td>
</tr>
<tr>

<td>Neem Urea Despatch</td><td><input id="neemureadespatch" onblur="calcNeemDespatch()"></td>

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

<input type="hidden" id="saverecord" name="saverecord">
<input type="hidden" id="error" value="<%=error%>">

<div class="footer">
<button type="button" onclick="saveForm()">Save</button>
<button type="button" onclick="window.close()">Exit</button>
</div>

</form>
</body>
</html>
