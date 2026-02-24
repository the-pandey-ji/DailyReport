<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%
    String reportDate = (String) request.getAttribute("reportdate");
    Double totalUrea = (Double) request.getAttribute("totalureaproduce");
    Double ureaOpening = (Double) request.getAttribute("ureaopeningstock");
    Double bagOpening = (Double) request.getAttribute("bagopeningstock");
    Double neemBagOpening = (Double) request.getAttribute("neembagopeningstock");
    Double neemOilOpening = (Double) request.getAttribute("neemoilopeningstock");
    String error = (String) request.getAttribute("error");

    if (reportDate == null) {
        reportDate = LocalDate.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
%>

<!DOCTYPE html>
<html>
<head>
<title>Urea Daily Report</title>

<style>
body { font-family: Arial; background:#eaeaea; }
.header { background:#b7d8ea; padding:6px; font-weight:bold; }
h3 { color:#800000; text-align:center; }
table { width:70%; margin:auto; }
td { padding:6px; }
input { width:90px; }
.footer { text-align:center; padding:15px; }
button { padding:4px 20px; }
</style>

<script>
var ureadaytarget = 1550;
var ureayeartarget = 511500;

/* ========= COMMON ========= */
function num(id){
    var v = document.getElementById(id).value;
    return v === "" ? 0 : parseFloat(v);
}

function checkno(el){
    if(el.value !== "" && isNaN(el.value)){
        alert("Only numbers allowed");
        el.value = "";
        el.focus();
    }
}

/* ========= CALCULATIONS ========= */
function calcUreaClose(){
    document.getElementById("ureaclosingstock").value =
        (num("ureaproduction") + num("ureaopeningstock")
        - num("rail") - num("road") - num("plainureatogoldurea")).toFixed(3);
}

function calcBagClose(){
    document.getElementById("bagclosingstock").value =
        num("bagopeningstock") + num("receipt") - num("consumption");
}

function calcNeemBagClose(){
    document.getElementById("neembagclosingstock").value =
        num("neembagopeningstock") + num("neemreceipt") - num("neemconsumption");
}

function calcNeemOilClose(){
    document.getElementById("neemoilclosingstock").value =
        (num("neemoilopeningstock") + num("neemoilreceipt")
        - num("neemoilconsumption")).toFixed(5);
}

function calcNeemDespatch(){
    document.getElementById("neemureadespatch").value =
        (num("rail") + num("road")).toFixed(2);
}

/* ========= SAVE ========= */
function saveForm(){
    document.getElementById("saverecord").value = "saveRecords";
    document.forms[0].submit();
}

/* ========= LOAD ========= */
function setForm(){
    calcUreaClose();
    calcBagClose();
    calcNeemBagClose();
    calcNeemOilClose();
}

/* ========= DATE CHANGE (NEW) ========= */
function loadDetails(){

    var date = document.getElementById("reportdate").value;
    var xhr = new XMLHttpRequest();

    xhr.open("GET","urea?action=details&reportdate="+encodeURIComponent(date),true);

    xhr.onreadystatechange = function(){
        if(xhr.readyState === 4 && xhr.status === 200){

            var res = xhr.responseText;

            if(res.startsWith("N?#?")){
                alert("No data for given date. You can enter new data.");
                clearEntryFields();
                setForm();
                return;
            }

            fillData(res.split("?#?"));
        }
    };
    xhr.send();
}

function clearEntryFields(){
    var ids = [
        "ureaproduction","neemureaproduction","co2consumption",
        "steamconsumption","steamhours",
        "rawmaterials","exportpower","mechanical","electrical",
        "instrumentation","process1","shutdown","others",
        "rail","road","receipt","consumption",
        "neemreceipt","neemconsumption",
        "neemoilreceipt","neemoilconsumption",
        "annualshutdown","plainureatogoldurea"
    ];
    ids.forEach(function(id){
        if(document.getElementById(id)) document.getElementById(id).value="";
    });
}

function fillData(d){
    var i=0;
    document.getElementById("ureaproduction").value=d[i++];
    document.getElementById("neemureaproduction").value=d[i++];
    document.getElementById("co2consumption").value=d[i++];
    document.getElementById("steamconsumption").value=d[i++];
    document.getElementById("steamhours").value=d[i++];
    document.getElementById("rawmaterials").value=d[i++];
    document.getElementById("exportpower").value=d[i++];
    document.getElementById("mechanical").value=d[i++];
    document.getElementById("electrical").value=d[i++];
    document.getElementById("instrumentation").value=d[i++];
    document.getElementById("process1").value=d[i++];
    document.getElementById("shutdown").value=d[i++];
    document.getElementById("others").value=d[i++];
    document.getElementById("rail").value=d[i++];
    document.getElementById("road").value=d[i++];
    document.getElementById("ureaopeningstock").value=d[i++];
    document.getElementById("ureaclosingstock").value=d[i++];
    document.getElementById("neemureadespatch").value=d[i++];
    document.getElementById("bagopeningstock").value=d[i++];
    document.getElementById("receipt").value=d[i++];
    document.getElementById("consumption").value=d[i++];
    document.getElementById("bagclosingstock").value=d[i++];
    document.getElementById("neemoilopeningstock").value=d[i++];
    document.getElementById("neemoilreceipt").value=d[i++];
    document.getElementById("neemoilconsumption").value=d[i++];
    document.getElementById("neemoilclosingstock").value=d[i++];
    document.getElementById("neembagopeningstock").value=d[i++];
    document.getElementById("neemreceipt").value=d[i++];
    document.getElementById("neemconsumption").value=d[i++];
    document.getElementById("neembagclosingstock").value=d[i++];
    document.getElementById("annualshutdown").value=d[i++];
    document.getElementById("plainureatogoldurea").value=d[i++];
}

document.addEventListener("DOMContentLoaded", function () {

	  const form = document.forms[0];

	  form.addEventListener("keydown", function (e) {

	    if (e.key === "Enter" && e.target.tagName !== "TEXTAREA") {
	      e.preventDefault();

	      const focusable = Array.from(
	        form.querySelectorAll(
	          'input:not([type=hidden]):not([readonly]):not([disabled]), select:not([disabled]), textarea:not([disabled])'
	        )
	      );

	      const index = focusable.indexOf(e.target);

	      if (index > -1 && index < focusable.length - 1) {
	        focusable[index + 1].focus();
	      }
	    }
	  });

	});
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
<td><input id="reportdate" name="reportdate" value="<%=reportDate%>" onchange="loadDetails()"></td>
<td>Total Urea Produce</td>
<td><input value="<%=totalUrea%>" readonly></td>
</tr>

<tr>
<td>Urea Production</td>
<td><input id="ureaproduction" name="ureaproduction" onblur="checkno(this);calcUreaClose()"></td>
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
<td><input id="steamhours" name="steamhours"></td>
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
<td>Rail</td><td><input id="rail" name="rail" onblur="calcUreaClose();calcNeemDespatch()"></td>
<td>Road</td><td><input id="road" name="road" onblur="calcUreaClose();calcNeemDespatch()"></td>
<td>Opening Stock</td><td><input id="ureaopeningstock" name="ureaopeningstock" value="<%=ureaOpening%>" readonly></td>
<td>Closing Stock</td><td><input id="ureaclosingstock" name="ureaclosingstock"></td>
</tr>
<tr>
<td>Neem Urea Despatch</td>
<td><input id="neemureadespatch" name="neemureadespatch" onblur="calcNeemDespatch()"></td>
</tr>
</table>

<h3>HDPE Bags</h3>
<table>
<tr>
<td>Opening</td><td><input id="bagopeningstock" name="bagopeningstock" value="<%=bagOpening%>" readonly></td>
<td>Receipt</td><td><input id="receipt" name="receipt" onblur="calcBagClose()"></td>
<td>Consumption</td><td><input id="consumption" name="consumption" onblur="calcBagClose()"></td>
<td>Closing</td><td><input id="bagclosingstock" name="bagclosingstock"></td>
</tr>
</table>

<h3>Neem Bags</h3>
<table>
<tr>
<td>Opening</td><td><input id="neembagopeningstock" name="neembagopeningstock" value="<%=neemBagOpening%>" readonly></td>
<td>Receipt</td><td><input id="neemreceipt" name="neemreceipt" onblur="calcNeemBagClose()"></td>
<td>Consumption</td><td><input id="neemconsumption" name="neemconsumption" onblur="calcNeemBagClose()"></td>
<td>Closing</td><td><input id="neembagclosingstock" name="neembagclosingstock"></td>
</tr>
</table>

<h3>Neem Oil</h3>
<table>
<tr>
<td>Opening</td><td><input id="neemoilopeningstock" name="neemoilopeningstock" value="<%=neemOilOpening%>" readonly></td>
<td>Receipt</td><td><input id="neemoilreceipt" name="neemoilreceipt" onblur="calcNeemOilClose()"></td>
<td>Consumption</td><td><input id="neemoilconsumption" name="neemoilconsumption" onblur="calcNeemOilClose()"></td>
<td>Closing</td><td><input id="neemoilclosingstock" name="neemoilclosingstock"></td>
</tr>
</table>

<input type="hidden" id="saverecord" name="saverecord">
<input type="hidden" id="error" value="<%=error%>">

<div class="footer">
<button type="button" onclick="saveForm()">Save</button>
<button type="button" onclick="location.href='index.jsp'">Exit</button>

</div>

</form>
</body>
</html>
