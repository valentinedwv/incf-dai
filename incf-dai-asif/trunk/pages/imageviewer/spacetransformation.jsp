<%@ page contentType="text/html;charset=WINDOWS-1252"%>
<%@ page import="java.util.ArrayList,java.util.Iterator,java.util.StringTokenizer" %>

<HTML>

<head>

<script language="javascript">
function setoptions(chosen)
{
var selbox = document.myform.opttwo;

var xtext = document.myform.rangeX;
var ytext = document.myform.rangeY;
var ztext = document.myform.rangeZ;

selbox.options.length = 0;
if (chosen == " ") {
selbox.options[selbox.options.length] = new Option('Select Src SRSCode first',' ');
}
if (chosen == "ABAVOXEL") {
document.getElementById("rangeX").innerHTML = "(0 to 527)";
document.getElementById("rangeY").innerHTML = "(0 to 319)";
document.getElementById("rangeZ").innerHTML = "(0 to 455)";
selbox.options[selbox.options.length] = new Option('AGEA','AGEA');
selbox.options[selbox.options.length] = new Option('ABAREFERENCE','ABAREFERENCE');
selbox.options[selbox.options.length] = new Option('WHS','WHS');
selbox.options[selbox.options.length] = new Option('PAXINOS','PAXINOS');
}
if (chosen == "AGEA") {
document.getElementById("rangeX").innerHTML = "(0 to 13175)";
document.getElementById("rangeY").innerHTML = "(0 to 7975)";
document.getElementById("rangeZ").innerHTML = "(0 to 11375)";
selbox.options[selbox.options.length] = new Option('ABAVOXEL','ABAVOXEL');
selbox.options[selbox.options.length] = new Option('WHS','WHS');
selbox.options[selbox.options.length] = new Option('PAXINOS','PAXINOS');
selbox.options[selbox.options.length] = new Option('ABAREFERENCE','ABAREFERENCE');
}
if (chosen == "WHS") {
document.getElementById("rangeX").innerHTML = "(0 to 511)";
document.getElementById("rangeY").innerHTML = "(0 to 1023)";
document.getElementById("rangeZ").innerHTML = "(0 to 511)";
selbox.options[selbox.options.length] = new Option('AGEA','AGEA');
selbox.options[selbox.options.length] = new Option('ABAVOXEL','ABAVOXEL');
selbox.options[selbox.options.length] = new Option('PAXINOS','PAXINOS');
selbox.options[selbox.options.length] = new Option('ABAREFERENCE','ABAREFERENCE');
}
if (chosen == "ABAREFERENCE") {
document.getElementById("rangeX").innerHTML = "(0 to 0)";
document.getElementById("rangeY").innerHTML = "(0 to 0)";
document.getElementById("rangeZ").innerHTML = "(0 to 0)";
selbox.options[selbox.options.length] = new Option('ABAVOXEL','ABAVOXEL');
selbox.options[selbox.options.length] = new Option('AGEA','AGEA');
selbox.options[selbox.options.length] = new Option('WHS','WHS');
selbox.options[selbox.options.length] = new Option('PAXINOS','PAXINOS');
}
if (chosen == "PAXINOS") {
document.getElementById("rangeX").innerHTML = "(-4.74 to 4.76)";
document.getElementById("rangeY").innerHTML = "(6.38 to -0.10)";
document.getElementById("rangeZ").innerHTML = "(-5.96 to 8.84)";
selbox.options[selbox.options.length] = new Option('WHS','WHS');
selbox.options[selbox.options.length] = new Option('ABAVOXEL','ABAVOXEL');
selbox.options[selbox.options.length] = new Option('AGEA','AGEA');
selbox.options[selbox.options.length] = new Option('ABAREFERENCE','ABAREFERENCE');
}
}
</script>
</head>

<BODY bgcolor="grey" onLoad="javascript:setoptions('ABAVOXEL')">

<FORM name="myform" METHOD="POST" ACTION="/incf-services/service/UCSDServiceController">

<input type="hidden" name="request" value="spaceTransformationView"/>
<input type="hidden" name="output" value="spacetransformationform"/>

<table align="center">
<tr>
<td align="center">
<b>Coordinate Transformations between Rodent Brain Reference Atlases</b>
</td>
</tr>

<tr>
<td>
<h5>(Note:This version of transformation web services is created as part of INCF-DAI development, to demonstrate service-based interoperability between rodent brain atlases. The project did not focus on transformation accuracy; the transformations presented here require further refinement) 
<h5>
</td>
</tr>
</table>
<table border="2" cellpadding="0" align="center" width="70%">
<tr>
<td align="center">
<strong>
Space Transformation
</strong>
</td>
</tr>
</table>
<table border="2" cellpadding="0" align="center" width="70%">

<tr>

<td rowspan="4" align="center">
<strong>
Source SRS Code:
<br>
<select width="30%" name="optone" size="1" STYLE="width: 200px"
onchange="setoptions(document.myform.optone.options[document.myform.optone.selectedIndex].value);">
<!--<option width="30%" value=" " selected="selected"> </option>-->
<option width="30%" value="ABAVOXEL" selected="selected">ABAVOXEL</option>
<option width="30%" value="AGEA">AGEA</option>
<option width="30%" value="WHS">WHS</option>
<option value="ABAREFERENCE">ABAREFERENCE</option>
<option value="PAXINOS">PAXINOS</option>
</select>
</td>

<td rowspan="4" align="center">
<strong>
Destination SRS Code:
<br>
<select name="opttwo" size="1" STYLE="width: 200px">
<!--<option value=" " selected="selected">Select Src SRSCode first</option> -->
</select>
</td>

<td>
<strong>
Coordinate X: &nbsp;&nbsp; <INPUT TYPE=TEXT value="" NAME="coordinateX" SIZE=10></strong>
<font size=-2><SPAN ID="rangeX"></SPAN></font>
</td>

<td rowspan="4" align="center">
<P><INPUT value="GO" TYPE=SUBMIT>
</td>

</tr>

<tr>
<td>
<strong>
Coordinate Y: &nbsp;&nbsp; <INPUT TYPE=TEXT value="" NAME="coordinateY" SIZE=10></strong>
<font size=-2><SPAN ID="rangeY"></SPAN></font>
</td>
</tr>

<tr>
<td>
<strong>
Coordinate Z: &nbsp;&nbsp; <INPUT TYPE=TEXT value="" NAME="coordinateZ" SIZE=10></strong>
<font size=-2><SPAN ID="rangeZ"></SPAN></font>
</td>
</tr>
</table>


<%

if ( request.getAttribute("response") != null  ) { 
edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO vo = (edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO)request.getAttribute("response");
System.out.println("Inside viewer 2");

%>
<table border="2" cellpadding="0" align="center" width="70%">
<tr>
<td align="center">
<strong>
View Transformation Data
</strong>
</td>
</tr>
</table>
<table border="2" cellpadding="0" align="center" width="70%">
<tr>
<td align="center">
<strong>
Source SRS Code
</strong>
</td>
<td align="center">
<strong>
X
</strong>
</td>
<td align="center">
<strong>
Y
</strong>
</td>
<td align="center">
<strong>
Z
</strong>
</td>
<td align="center">
<strong>
Destination SRS Code
</strong>
</td>
<td align="center">
<strong>
X
</strong>
</td>
<td align="center">
<strong>
Y
</strong>
</td>
<td align="center">
<strong>
Z
</strong>
</td>
</tr>

<%

		String transformationOne = vo.getTransformationOne();
		StringTokenizer tokensOne = new StringTokenizer(transformationOne, " ");
		String[] transformationOneData = new String[tokensOne.countTokens()];
		int i = 0;
		while( tokensOne.hasMoreTokens() ) {
			transformationOneData[i] = tokensOne.nextToken();
			i++;
		}

		if ( !transformationOneData[5].equalsIgnoreCase("out") ) { 

%>

<tr>
<td>
<%=transformationOneData[0]%>
</td>
<td>
<%=transformationOneData[2]%>
</td>
<td>
<%=transformationOneData[3]%>
</td>
<td>
<%=transformationOneData[4]%>
</td>
<td>
<%=transformationOneData[1]%>
</td>
<td>
<%=transformationOneData[5]%>
</td>
<td>
<%=transformationOneData[6]%>
</td>
<td>
<%=transformationOneData[7]%>
</td>
</tr>



<%
}
%>

<%

		if ( vo.getTransformationTwo() != null ) { 

		String transformationTwo = vo.getTransformationTwo();
		StringTokenizer tokensTwo = new StringTokenizer(transformationTwo, " ");
		String[] transformationTwoData = new String[tokensTwo.countTokens()];
		int j = 0;
		while( tokensTwo.hasMoreTokens() ) {
			transformationTwoData[j] = tokensTwo.nextToken();
			j++;
		}

		if ( !transformationTwoData[5].equalsIgnoreCase("out") ) { 

%>

<tr>
<td>
<%=transformationTwoData[0]%>
</td>
<td>
<%=transformationTwoData[2]%>
</td>
<td>
<%=transformationTwoData[3]%>
</td>
<td>
<%=transformationTwoData[4]%>
</td>
<td>
<%=transformationTwoData[1]%>
</td>
<td>
<%=transformationTwoData[5]%>
</td>
<td>
<%=transformationTwoData[6]%>
</td>
<td>
<%=transformationTwoData[7]%>
</td>
</tr>


<%
}
}
%>

<%

		if ( vo.getTransformationThree() != null ) { 

		String transformationThree = vo.getTransformationThree();
		StringTokenizer tokensThree = new StringTokenizer(transformationThree, " ");
		String[] transformationThreeData = new String[tokensThree.countTokens()];
		int k = 0;
		while( tokensThree.hasMoreTokens() ) {
			transformationThreeData[k] = tokensThree.nextToken();
			k++;
		}

		if ( !transformationThreeData[5].equalsIgnoreCase("out") ) { 

%>

<tr>
<td>
<%=transformationThreeData[0]%>
</td>
<td>
<%=transformationThreeData[2]%>
</td>
<td>
<%=transformationThreeData[3]%>
</td>
<td>
<%=transformationThreeData[4]%>
</td>
<td>
<%=transformationThreeData[1]%>
</td>
<td>
<%=transformationThreeData[5]%>
</td>
<td>
<%=transformationThreeData[6]%>
</td>
<td>
<%=transformationThreeData[7]%>
</td>
</tr>


<%
}
}
%>

<%

		if ( vo.getTransformationFour() != null ) { 

		String transformationFour = vo.getTransformationFour();
		StringTokenizer tokensFour = new StringTokenizer(transformationFour, " ");
		String[] transformationFourData = new String[tokensFour.countTokens()];
		int l = 0;
		while( tokensFour.hasMoreTokens() ) {
			transformationFourData[l] = tokensFour.nextToken();
			l++;
		}

		if ( !transformationFourData[5].equalsIgnoreCase("out") ) { 

%>

<tr>
<td>
<%=transformationFourData[0]%>
</td>
<td>
<%=transformationFourData[2]%>
</td>
<td>
<%=transformationFourData[3]%>
</td>
<td>
<%=transformationFourData[4]%>
</td>
<td>
<%=transformationFourData[1]%>
</td>
<td>
<%=transformationFourData[5]%>
</td>
<td>
<%=transformationFourData[6]%>
</td>
<td>
<%=transformationFourData[7]%>
</td>
</tr>


<%
}
}
%>
</table>

<table border="2" cellpadding="0" align="center" width="70%">

<tr>
<td align="center">
<strong>
XML Representation
</strong>
</td>
</tr>

</table>

<table border="2" cellpadding="0" align="center" width="70%">

<tr>
<td style="width: 750px;">
<div style="overflow:auto; width:775px; height: 200px;"> 
<xmp>
<%=vo.getXmlStringForTransformationInfo()%>
</xmp>
</div>
</td>
</tr>

<%
}
%>

</table>


</FORM>

</BODY>
</HTML>
