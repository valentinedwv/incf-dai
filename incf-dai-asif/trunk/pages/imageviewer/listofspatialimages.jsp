<%@ page contentType="text/html;charset=WINDOWS-1252"%>
<%@ page import="java.util.ArrayList,java.util.Iterator" %>

<HTML>
  <HEAD>
  <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=WINDOWS-1252">
  <META NAME="GENERATOR" CONTENT="Oracle9i JDeveloper">
    <TITLE>MAPSERVER</TITLE>
  </HEAD>
    <BODY BGCOLOR=GREY>
       <P>
       <BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
       <B><CENTER><FONT SIZE=5>
         <TABLE BORDER BGCOLOR=GREY>
            <TR>
               <TD ALIGN=CENTER VALIGN=CENTER>
                  <B><FONT COLOR=BLACK SIZE=5>
					List of Images:
				  </B>
               </TD>
            </TR>

<% 
ArrayList list = (ArrayList)request.getAttribute("completeImageList");
Iterator iterator = list.iterator();
edu.ucsd.crbs.incf.common.CommonServiceVO vo = new edu.ucsd.crbs.incf.common.CommonServiceVO();
while ( iterator.hasNext() ) {
	vo = ( edu.ucsd.crbs.incf.common.CommonServiceVO )iterator.next();
%>
            <TR>
               <TD ALIGN=CENTER VALIGN=CENTER>
                  <FONT COLOR=BLACK SIZE=2>
		<a href="http://smartatlas.crbs.ucsd.edu:8080/mapserver-services/service/MapserverServiceController?request=Get2DImage&imageServiceName=<%= vo.getImageServiceName() %>&imageName=<%= vo.getImageBaseName() %>&SRSCode=ucsd&output=html">
		<%= vo.getImageBaseName() %></a>

               </TD>
            </TR>
<%
}
%>

		 </TABLE>
         </CENTER></B>
       </P>
     </BODY>
</HTML>
