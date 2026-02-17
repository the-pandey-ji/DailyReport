<%@ page contentType="text/html;charset=UTF-8" %>
<%
String date = (String)request.getAttribute("reportdate");
%>

<!DOCTYPE html>
<html>
<head>
<title>Offsites & Utilities Daily Report</title>

<style>
body{font-family:Arial;background:#eee}
h3{text-align:center;color:#800000;margin-top:20px}
table{width:95%;margin:auto;border-collapse:collapse}
td{padding:6px}
input{width:90px}
.header{background:#b7d8ea;padding:6px;font-weight:bold}
.footer{text-align:center;margin:20px}
</style>

<script>
function num(id){
    return parseFloat(document.getElementById(id).value || 0);
}
function calc(op,r,c,cl){
    document.getElementById(cl).value =
        (num(op)+num(r)-num(c)).toFixed(3);
}

/* ================= NEW (1) ================= */
function clearFields(){
 [
  "h2so4_rct","h2so4_cons","h2so4_cl_stk",
  "naoh_rct","naoh_cons","naoh_cl_stk",
  "bs_rct","bs_cons","bs_cl_stk",
  "ms_rct","ms_cons","ms_cl_stk",
  "bc_rct","bc_cons","bc_cl_stk",
  "bs_bag_rct","bs_bag_cons","bs_bag_cl_stk",
  "steam"
 ].forEach(id=>{
    if(document.getElementById(id))
        document.getElementById(id).value="";
 });
}

function loadDetails(){
    var d=document.getElementById("reportdate").value;
    var x=new XMLHttpRequest();
    x.open("GET","offsites?action=details&reportdate="+encodeURIComponent(d),true);
    x.onload=function(){

        /* ================= NEW (2) ================= */
        if(this.responseText.startsWith("N")){
            alert("No data for given date. You can enter new data.");
            clearFields();
            return;
        }

        var a=this.responseText.split("?#?");
        var i=0;
        [
        "h2so4_op_stk","h2so4_rct","h2so4_cons","h2so4_cl_stk",
        "naoh_op_stk","naoh_rct","naoh_cons","naoh_cl_stk",
        "bs_op_stk","bs_rct","bs_cons","bs_cl_stk",
        "ms_op_stk","ms_rct","ms_cons","ms_cl_stk",
        "bc_op_stk","bc_rct","bc_cons","bc_cl_stk",
        "bs_bag_op_stk","bs_bag_rct","bs_bag_cons","bs_bag_cl_stk",
        "steam"
        ].forEach(id=>{
            if(document.getElementById(id))
                document.getElementById(id).value=a[i++];
        });
    }
    x.send();
}

/* ================= NEW (3) ================= */
window.onload=function(){
<% if(request.getAttribute("h2so4_op")!=null){ %>
document.getElementById("h2so4_op_stk").value="<%=request.getAttribute("h2so4_op")%>";
document.getElementById("naoh_op_stk").value="<%=request.getAttribute("naoh_op")%>";
document.getElementById("bs_op_stk").value="<%=request.getAttribute("bs_op")%>";
document.getElementById("ms_op_stk").value="<%=request.getAttribute("ms_op")%>";
document.getElementById("bc_op_stk").value="<%=request.getAttribute("bc_op")%>";
document.getElementById("bs_bag_op_stk").value="<%=request.getAttribute("bs_bag_op")%>";
<% } %>
};
</script>
</head>

<body>

<div class="header">
Department : IT
<span style="float:right">Date : <%=date%></span>
</div>

<h3>Offsites & Utilities Daily Report</h3>

<form method="post" action="offsites">

<table>
<tr>
<td>Report Date</td>
<td><input id="reportdate" name="reportdate" value="<%=date%>" onchange="loadDetails()"></td>
</tr>
</table>

<h3>Chemicals</h3>
<table border="1">
<tr>
<th></th><th>Opening Stock</th><th>Receipt/Prodn</th>
<th>Transfer/Consm</th><th>Closing Stock</th>
</tr>

<tr>
<td>H2SO4</td>
<td><input id="h2so4_op_stk" readonly></td>
<td><input id="h2so4_rct" name="h2so4_rct"
 onblur="calc('h2so4_op_stk','h2so4_rct','h2so4_cons','h2so4_cl_stk')"></td>
<td><input id="h2so4_cons" name="h2so4_cons"
 onblur="calc('h2so4_op_stk','h2so4_rct','h2so4_cons','h2so4_cl_stk')"></td>
<td><input id="h2so4_cl_stk" name="h2so4_cl"></td>
</tr>

<tr>
<td>NaOH</td>
<td><input id="naoh_op_stk" readonly></td>
<td><input id="naoh_rct" name="naoh_rct"
 onblur="calc('naoh_op_stk','naoh_rct','naoh_cons','naoh_cl_stk')"></td>
<td><input id="naoh_cons" name="naoh_cons"
 onblur="calc('naoh_op_stk','naoh_rct','naoh_cons','naoh_cl_stk')"></td>
<td><input id="naoh_cl_stk" name="naoh_cl"></td>
</tr>
</table>

<h3>Bentonite Sulphur Plant</h3>
<table border="1">
<tr>
<th></th><th>Opening</th><th>Receipt/Prodn</th>
<th>Dispatch/Cons.</th><th>Closing Stock</th>
</tr>

<tr>
<td>Bentonite Sulphur</td>
<td><input id="bs_op_stk" readonly></td>
<td><input id="bs_rct" name="bs_rct"
 onblur="calc('bs_op_stk','bs_rct','bs_cons','bs_cl_stk')"></td>
<td><input id="bs_cons" name="bs_cons"
 onblur="calc('bs_op_stk','bs_rct','bs_cons','bs_cl_stk')"></td>
<td><input id="bs_cl_stk" name="bs_cl"></td>
</tr>

<tr>
<td>Molten Sulphur</td>
<td><input id="ms_op_stk" readonly></td>
<td><input id="ms_rct" name="ms_rct"
 onblur="calc('ms_op_stk','ms_rct','ms_cons','ms_cl_stk')"></td>
<td><input id="ms_cons" name="ms_cons"
 onblur="calc('ms_op_stk','ms_rct','ms_cons','ms_cl_stk')"></td>
<td><input id="ms_cl_stk" name="ms_cl"></td>
</tr>

<tr>
<td>Bentonite Clay</td>
<td><input id="bc_op_stk" readonly></td>
<td><input id="bc_rct" name="bc_rct"
 onblur="calc('bc_op_stk','bc_rct','bc_cons','bc_cl_stk')"></td>
<td><input id="bc_cons" name="bc_cons"
 onblur="calc('bc_op_stk','bc_rct','bc_cons','bc_cl_stk')"></td>
<td><input id="bc_cl_stk" name="bc_cl"></td>
</tr>

<tr>
<td>Bentonite Sulphur Bag</td>
<td><input id="bs_bag_op_stk" readonly></td>
<td><input id="bs_bag_rct" name="bs_bag_rct"
 onblur="calc('bs_bag_op_stk','bs_bag_rct','bs_bag_cons','bs_bag_cl_stk')"></td>
<td><input id="bs_bag_cons" name="bs_bag_cons"
 onblur="calc('bs_bag_op_stk','bs_bag_rct','bs_bag_cons','bs_bag_cl_stk')"></td>
<td><input id="bs_bag_cl_stk" name="bs_bag_cl"></td>
</tr>

<tr>
<td>Steam Consumption</td>
<td colspan="4"><input id="steam" name="steam"></td>
</tr>
</table>

<input type="hidden" name="saverecord" value="saveRecords">

<div class="footer">
<button type="submit">Save</button>
<button type="button" onclick="window.close()">Exit</button>
</div>

</form>
</body>
</html>
