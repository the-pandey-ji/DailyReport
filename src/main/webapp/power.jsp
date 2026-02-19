<%@ page contentType="text/html;charset=UTF-8" %>
<%
String reportDate = (String)request.getAttribute("report_date");
if(reportDate == null) reportDate = "";
String error = request.getAttribute("error")==null?"":request.getAttribute("error").toString();
%>

<!DOCTYPE html>
<html>
<head>
<title>Power Daily Report Data Entry Form</title>

<style>
body{font-family:Arial;background:#eee}
h3{text-align:center;color:#800000;margin-top:20px}
table{width:65%;margin:auto;border-collapse:collapse}
td{padding:6px}
input{width:90px}
.header{background:#b7d8ea;padding:6px;font-weight:bold}
.footer{text-align:center;margin:20px}
</style>

<script>
function checkno(el){
    if(el.value==="") return;
    if(isNaN(el.value)){
        alert("Please enter numeric value");
        el.value="";
        el.focus();
    }
}

function getDetail(){
    var d = document.getElementById("reportdate").value;
    if(d===""){
        alert("Please enter date");
        return;
    }

    var x = new XMLHttpRequest();
    x.open("GET","power?action=details&reportdate="+encodeURIComponent(d),true);
    x.onload=function(){
        var arr=this.responseText.split("?#?");
        if(arr[0]==="N"){
            alert("No Data available");
            return;
        }
        document.getElementById("powerimported").value = arr[0];
        document.getElementById("TG1").value = arr[1];
        document.getElementById("TG2").value = arr[2];
        document.getElementById("powerinunit").value = arr[3];
        document.getElementById("powerincpp").value = arr[4];
        document.getElementById("ammoniapower").value = arr[5];
        document.getElementById("gtgpowergen").value = arr[6];
        document.getElementById("gtgpowercons").value = arr[7];
        document.getElementById("gtgpowerexp").value = arr[8];
    }
    x.send();
}

function save(){
    document.PowerEntry.jmethod.value="saveRecords";
    document.PowerEntry.submit();
}

function setForm(){
    if("<%=error%>"==="N"){
        alert("You are not authorized");
        document.querySelectorAll("input").forEach(i=>i.disabled=true);
    }
}
</script>
</head>

<body onload="setForm()">

<div class="header">
Department : IT
<span style="float:right">Date : <%=reportDate%></span>
</div>

<h3>Power Daily Report Data Entry Form</h3>

<form name="PowerEntry" method="post" action="power">
<input type="hidden" name="jmethod">
<input type="hidden" name="error" value="<%=error%>">

<table>
<tr>
<td><b>Report Date</b></td>
<td><input id="reportdate" name="reportdate" value="<%=reportDate%>" onchange="getDetail()"></td>
</tr>

<tr>
<td>Power Imported</td>
<td><input id="powerimported" name="powerimported" onblur="checkno(this)"></td>
</tr>

<tr>
<td>CPP Power generated in TG-1</td>
<td><input id="TG1" name="TG1" onblur="checkno(this)"></td>
<td>TG-2</td>
<td><input id="TG2" name="TG2" onblur="checkno(this)"></td>
</tr>

<tr>
<td>Power Consumed in Unit</td>
<td><input id="powerinunit" name="powerinunit" onblur="checkno(this)"></td>
<td>Power Consumed in CPP</td>
<td><input id="powerincpp" name="powerincpp" onblur="checkno(this)"></td>
</tr>

<tr>
<td>Ammonia Power (MW)</td>
<td><input id="ammoniapower" name="ammoniapower" onblur="checkno(this)"></td>
</tr>

<tr>
<td>GTG Power Generation</td>
<td><input id="gtgpowergen" name="gtgpowergen" onblur="checkno(this)"></td>
<td>Power Consumption in GTG/HRSG</td>
<td><input id="gtgpowercons" name="gtgpowercons" onblur="checkno(this)"></td>
</tr>

<tr>
<td>GTG Power Export</td>
<td><input id="gtgpowerexp" name="gtgpowerexp" onblur="checkno(this)"></td>
</tr>
</table>

<div class="footer">
<button type="button" onclick="save()">Save</button>
<button type="button" onclick="location.href='index.jsp'">Exit</button>
</div>

</form>
</body>
</html>
