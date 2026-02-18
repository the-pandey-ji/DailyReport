<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%
String reportDate = (String)request.getAttribute("reportdate");
Double totalAmmonia = (Double)request.getAttribute("totalammonia");
Double prevStock = (Double)request.getAttribute("prevstock");

if(reportDate == null){
    reportDate = LocalDate.now()
        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
}
%>

<!DOCTYPE html>
<html>
<head>
<title>Ammonia Daily Report</title>

<style>
body{font-family:Arial;background:#eee}
h3{text-align:center;color:#800000}
table{width:95%;margin:auto;border-collapse:collapse}
td{padding:6px}
input{width:110px}
.header{background:#b7d8ea;padding:6px;font-weight:bold}
.footer{text-align:center;margin:20px}
</style>

<script>
var ammoniadaytarget=900;
var ammoniayeartarget=297000;

function n(id){ return parseFloat(document.getElementById(id).value||0); }

function calcClosing(){
    document.getElementById("ammonia_closing_stock").value =
      (n("previous_day_ammonia_stock")+n("ammonia_production")+n("ammonia_import")
      -n("ammonia_to_urea")-n("ammonia_internal_consumption")
      -n("ammonia_export_to_units")).toFixed(2);
}

function loadDetails(){
    var d=document.getElementById("reportdate").value;
    var x=new XMLHttpRequest();
    x.open("GET","ammonia?action=details&reportdate="+encodeURIComponent(d),true);
    x.onload=function(){
        if(this.responseText.startsWith("N")){
            alert("No data for selected date");
            return;
        }
        var a=this.responseText.split("?#?");
        var i=0;
        [
        	 "ammonia_production","ammonia_import","ammonia_to_urea",
        	 "ammonia_internal_consumption","ammonia_export_to_units",
        	 "ammonia_closing_stock","previous_day_ammonia_stock",
        	 "ammonia_stream_hours","feed_consumption_ng","bfw_export_to_sgp",
        	 "amdea_consumption","bfw_export_to_cpp","whb_steam_to_100k_hdr",
        	 "average_bfw_temperature","cv_ng",
        	 "rawmaterials","exportpower","mechanical","electrical",
        	 "instrumentation","process1","shutdown","others",
        	 "totalng","annualshutdown",
        	 "totalammoniaproduce",
        	 "ngconsgtg"
        	].forEach(id=>{
            if(document.getElementById(id))
                document.getElementById(id).value=a[i++];
        });
    }
    x.send();
}

window.onload = function () {
    var d = document.getElementById("reportdate");
    if (d && d.value !== "") {
        loadDetails();
    }
};

</script>
</head>

<body>

<div class="header">
Department : IT
<span style="float:right">Date : <%=reportDate%></span>
</div>

<h3>Ammonia Daily Report Data Entry Form</h3>

<form method="post" action="ammonia">

<table>
<tr>
<td>Report Date</td>
<td><input id="reportdate" name="reportdate" value="<%=reportDate%>" onchange="loadDetails()"></td>
<td>Total Ammonia Produce</td>
<td><input id="totalammoniaproduce" value="<%=totalAmmonia%>" readonly></td>
</tr>

<tr>
<td>Ammonia Production</td>
<td><input id="ammonia_production" name="ammonia_production" onblur="calcClosing()"></td>
<td>Ammonia Import</td>
<td><input id="ammonia_import" name="ammonia_import" onblur="calcClosing()"></td>
<td>Ammonia to Urea</td>
<td><input id="ammonia_to_urea" name="ammonia_to_urea" onblur="calcClosing()"></td>
</tr>

<tr>
<td>Internal Consumption</td>
<td><input id="ammonia_internal_consumption" name="ammonia_internal_consumption" onblur="calcClosing()"></td>
<td>Export to Units</td>
<td><input id="ammonia_export_to_units" name="ammonia_export_to_units" onblur="calcClosing()"></td>
<td>Closing Stock</td>
<td><input id="ammonia_closing_stock" name="ammonia_closing_stock" readonly></td>
</tr>

<tr>
<td>Previous Day Stock</td>
<td><input id="previous_day_ammonia_stock" value="<%=prevStock%>" readonly></td>
<td>Stream Hours</td>
<td><input id="ammonia_stream_hours" name="ammonia_stream_hours"></td>
<td>Feed Cons (NG)</td>
<td><input id="feed_consumption_ng" name="feed_consumption_ng"></td>
</tr>

<tr>
<td>BFW Export SGP</td>
<td><input id="bfw_export_to_sgp" name="bfw_export_to_sgp"></td>
<td>aMDEA Cons</td>
<td><input id="amdea_consumption" name="amdea_consumption"></td>
<td>BFW Export CPP</td>
<td><input id="bfw_export_to_cpp" name="bfw_export_to_cpp" value="0" ></td>
</tr>

<tr>
<td>WHB Steam 100K</td>
<td><input id="whb_steam_to_100k_hdr" name="whb_steam_to_100k_hdr"></td>
<td>Avg BFW Temp</td>
<td><input id="average_bfw_temperature" name="average_bfw_temperature"></td>
<td>CV NG</td>
<td><input id="cv_ng" name="cv_ng"></td>
</tr>

<tr>
<td>Total NG Received</td>
<td><input id="totalng" name="totalng"></td>
</tr>
<tr>
<td>NG Cons GTG</td>
<td><input id="ngconsgtg" name="ngconsgtg"></td>
</tr>
</table>

<h3>Production Loss</h3>
<table>
<tr>
<td>Raw Materials</td><td><input id="rawmaterials" name="rawmaterials"></td>
<td>Export Power</td><td><input id="exportpower" name="exportpower"></td>
<td>Mechanical</td><td><input id="mechanical" name="mechanical"></td>
<td>Electrical</td><td><input id="electrical" name="electrical"></td>
</tr>
<tr>
<td>Instrumentation</td><td><input id="instrumentation" name="instrumentation"></td>
<td>Process</td><td><input id="process1" name="process1"></td>
<td>Shutdown</td><td><input id="shutdown" name="shutdown"></td>
<td>Others</td><td><input id="others" name="others"></td>
</tr>
<tr>
<td>Annual Shutdown</td><td><input id="annualshutdown" name="annualshutdown"></td>
</tr>
</table>

<input type="hidden" name="saverecord" value="saveRecords">

<div class="footer">
<button type="submit">Save</button>
<button type="button" onclick="location.href='index.jsp'">Exit</button>

</div>

</form>
</body>
</html>
