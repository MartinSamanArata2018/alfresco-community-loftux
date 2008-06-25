<#import "import/alfresco-template.ftl" as template />
<#import "import/alfresco-layout.ftl" as layout />
<@template.header>
   <link rel="stylesheet" type="text/css" href="${url.context}/themes/${theme}/dashboard.css" />
</@>

<@template.body>
   <div id="hd">
      <@region id="header" scope="global" protected=true />
      <@region id="title" scope="page" protected=true />
      <div>
         <h1>${page.title}</h1>
      </div>
   </div>
   <div id="bd">
      <@layout.grid gridColumns gridClass "component" />
   </div>
</@>

<@template.footer>
   <div id="ft">
      <@region id="footer" scope="global" protected=true />
   </div>
</@>