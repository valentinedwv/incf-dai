<%@ page contentType="text/html;charset=WINDOWS-1252"%>
<HTML>
  <HEAD>
  <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=WINDOWS-1252">
  <META NAME="GENERATOR" CONTENT="Oracle9i JDeveloper">
    <TITLE>INCF</TITLE>
  </HEAD>
    <BODY BGCOLOR=GREY>
       <P>
       <BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
       <B><CENTER><FONT SIZE=5>
         <TABLE BORDER BGCOLOR=GREY>
            <TR>
               <TD ALIGN=CENTER VALIGN=CENTER>
                  <B><FONT COLOR=BLACK SIZE=5>
                     <!-- &nbsp;<% out.println("Technical difficulties. The administrator has been notified. Please try after some time."); %> -->
					
<%

if ( request.getAttribute("responseVO") != null  ) { 
edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO vo = (edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO)request.getAttribute("responseVO");
%>
	<%=vo.getErrorMessage() %>
<%
} else if ( request.getAttribute("response") != null ) { 
%>
	<%= request.getAttribute("response") %>
<%
}
%>
				  </B>
               </TD>
            </TR>
         </TABLE>
         </CENTER></B>
       </P>
     </BODY>
</HTML>