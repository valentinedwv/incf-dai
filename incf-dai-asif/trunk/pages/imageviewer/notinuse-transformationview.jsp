<%@ page contentType="text/html;charset=WINDOWS-1252"%>
<%@ page import="java.util.ArrayList,java.util.Iterator" %>

<HTML>

<BODY bgcolor="grey" onLoad="javascript:setoptions('ABAVOXEL')">

<FORM name="myform" METHOD=POST ACTION="/incf-services/service/UCSDServiceController">

<input type="hidden" name="request" value="spaceTransformationForm"/>
<input type="hidden" name="output" value="form"/>

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

<table border="2" cellpadding="0" align="center">

<%
if ( request.getAttribute("response") == null  ) { 
%>

<tr>
<td>
Transformed Coordinate X: <INPUT TYPE=TEXT NAME="coordinateX" value="" SIZE=20>
</td>
</tr>
<tr>
<td>
Transformed Coordinate Y: <INPUT TYPE=TEXT NAME="coordinateY" value="" SIZE=20>
</td>
</tr>
<tr>
<td>
Transformed Coordinate Z: <INPUT TYPE=TEXT NAME="coordinateZ" value="" SIZE=20>
</td>
</tr>

<%

} else if ( request.getAttribute("response") != null  ) { 
edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO vo = (edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO)request.getAttribute("response");
System.out.println("Inside viewer 2");

%>

<tr>
<td>
Source SRS Code: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <INPUT TYPE=TEXT NAME="srcSRSCode" value="<%=vo.getSrcSRSCode()%>" SIZE=20>
</td>
</tr>
<tr>
<td>
Destination SRS Code: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT TYPE=TEXT NAME="destSRSCode" value="<%=vo.getDestSRSCode()%>" SIZE=20>
</td>
</tr>

<tr>
<td>
Source Coordinate X: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT TYPE=TEXT NAME="coordinateX" value="<%=vo.getOriginalCoordinateX()%>" SIZE=20>
</td>
</tr>
<tr>
<td>
Source Coordinate Y: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT TYPE=TEXT NAME="coordinateX" value="<%=vo.getOriginalCoordinateY()%>" SIZE=20>
</td>
</tr>
<tr>
<td>
Source Coordinate Z: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT TYPE=TEXT NAME="coordinateX" value="<%=vo.getOriginalCoordinateZ()%>" SIZE=20>
</td>
</tr>

<tr>
<td>
Transformed Coordinate X: <INPUT TYPE=TEXT NAME="transCoordinateX" value="<%=vo.getTransformedCoordinateX()%>" SIZE=20>
</td>
</tr>
<tr>
<td>
Transformed Coordinate Y: <INPUT TYPE=TEXT NAME="transCoordinateY" value="<%=vo.getTransformedCoordinateY()%>" SIZE=20>
</td>
</tr>
<tr>
<td>
Transformed Coordinate Z: <INPUT TYPE=TEXT NAME="transCoordinateZ" value="<%=vo.getTransformedCoordinateZ()%>" SIZE=20>
</td>
</tr>

<%
}
%>

</table>
</FORM>

</BODY>
</HTML>
