package org.alfresco.share.enterprise.wqs.web.search;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.alfresco.po.share.site.document.ContentDetails;
import org.alfresco.po.share.site.document.ContentType;
import org.alfresco.po.share.site.document.DocumentLibraryPage;
import org.alfresco.po.share.wqs.WcmqsBlogPostPage;
import org.alfresco.po.share.wqs.WcmqsHomePage;
import org.alfresco.po.share.wqs.WcmqsSearchPage;
import org.alfresco.share.util.AbstractUtils;
import org.alfresco.share.util.ShareUser;
import org.alfresco.webdrone.testng.listener.FailedTestListener;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Created by Cristina Axinte on 12/22/2014.
 */

@Listeners(FailedTestListener.class)
public class SearchTests extends AbstractUtils
{
    private static final Logger logger = Logger.getLogger(SearchTests.class);
    private final String ALFRESCO_QUICK_START = "Alfresco Quick Start";
    private final String QUICK_START_EDITORIAL = "Quick Start Editorial";
    private final String ROOT = "root";

    private String testName;
    private String wqsURL;
    private String siteName;
    private String ipAddress;
    private String hostName;
    private String blogTitle1;
    private String blogTitle2;
    private String blogTitle3;
    private String blogHouse5710;
    private String blogTechno5710;
    private String blogTrance5710;
    private String newsHouse5710;
    private String newsTechno5710;
    private String newsTrance5710;
    private String publicationHouse5710;
    private String publicationTechno5710;
    private String publicationTrance5710;

    private String blogHouse5711;
    private String blogTechno5711;
    private String blogTrance5711;
    private String newsHouse5711;
    private String newsTechno5711;
    private String newsTrance5711;
    private String publicationHouse5711;
    private String publicationTechno5711;
    private String publicationTrance5711;

    private String tagHouse = "House2";
    private String tagTechno = "techno2";
    private String tagTrance = "trance2";

    @Override
    @BeforeClass(alwaysRun = true)
    public void setup() throws Exception
    {
        super.setup();

        testName = this.getClass().getSimpleName();
        siteName = testName;
        blogTitle1 = "Our top analyst's latest thoughts";
        blogTitle2 = "Company organises workshop";
        blogTitle3 = "Ethical funds";

        blogHouse5710 = "Blog House5710";
        blogTechno5710 = "Blog Techno5710";
        blogTrance5710 = "Blog Trance5710";
        newsHouse5710 = "News House5710";
        newsTechno5710 = "News Techno5710";
        newsTrance5710 = "News Trance5710";
        publicationHouse5710 = "Publication House5710";
        publicationTechno5710 = "Publication Techno5710";
        publicationTrance5710 = "Publication Trance5710";

        blogHouse5711 = "Blog house1 content 5711";
        blogTechno5711 = "Blog techno1 content 5711";
        blogTrance5711 = "Blog trance1 content 5711";
        newsHouse5711 = "News House1 content 5711";
        newsTechno5711 = "News Techno1 content 5711";
        newsTrance5711 = "News Trance1 content 5711";
        publicationHouse5711 = "Publication House1 content 5711";
        publicationTechno5711 = "Publication Techno1 content 5711";
        publicationTrance5711 = "Publication Trance1 content 5711";

        hostName = (shareUrl).replaceAll(".*\\//|\\:.*", "");
        try
        {
            ipAddress = InetAddress.getByName(hostName).toString().replaceAll(".*/", "");
            logger.info("Ip address from Alfresco server was obtained");
        }
        catch (UnknownHostException | SecurityException e)
        {
            logger.error("Ip address from Alfresco server could not be obtained");
        }

        ;
        wqsURL = siteName + ":8080/wcmqs";
        logger.info(" wcmqs url : " + wqsURL);
        logger.info("Start Tests from: " + testName);
    }

    @Test(groups = { "DataPrepWQS" })
    public void dataPrep_AONE() throws Exception
    {
        // User login
        // ---- Step 1 ----
        // ---- Step Action -----
        // WCM Quick Start is installed; - is not required to be executed automatically
        ShareUser.login(drone, ADMIN_USERNAME, ADMIN_PASSWORD);

        // ---- Step 2 ----
        // ---- Step Action -----
        // Site "My Web Site" is created in Alfresco Share;
        DocumentLibraryPage documentLibPage = ShareUser.openSiteDocumentLibraryFromSearch(drone, siteName);
        // ShareUser.createSite(drone, siteName, SITE_VISIBILITY_PUBLIC);
        //
        // // ---- Step 3 ----
        // // ---- Step Action -----
        // // WCM Quick Start Site Data is imported;
        // SiteDashboardPage siteDashBoard = ShareUserDashboard.addDashlet(drone, siteName, Dashlets.WEB_QUICK_START);
        // SiteWebQuickStartDashlet wqsDashlet = siteDashBoard.getDashlet(SITE_WEB_QUICK_START_DASHLET).render();
        // wqsDashlet.selectWebsiteDataOption(WebQuickStartOptions.FINANCE);
        // wqsDashlet.clickImportButtton();
        //
        // // Change property for quick start to sitename
        // DocumentLibraryPage documentLibPage = ShareUser.openSitesDocumentLibrary(drone, siteName);
        // documentLibPage.selectFolder(ALFRESCO_QUICK_START);
        // EditDocumentPropertiesPage documentPropertiesPage = documentLibPage.getFileDirectoryInfo(QUICK_START_EDITORIAL).selectEditProperties().render();
        // documentPropertiesPage.setSiteHostname(siteName);
        // documentPropertiesPage.clickSave();
        //
        // // Change property for quick start live to ip address
        // documentLibPage.getFileDirectoryInfo("Quick Start Live").selectEditProperties().render();
        // documentPropertiesPage.setSiteHostname(ipAddress);
        // documentPropertiesPage.clickSave();

        // Several articles are created in News, Publications, Blogs components:
        // * blogs: house, techno, trance with any content
        documentLibPage.selectFolder(ALFRESCO_QUICK_START);
        documentLibPage.selectFolder(QUICK_START_EDITORIAL);
        documentLibPage.selectFolder(ROOT);
        documentLibPage.selectFolder("blog");

        ContentDetails contentDetails1 = new ContentDetails();
        contentDetails1.setName("BlogH5710.html");
        contentDetails1.setContent("content blog h1");
        contentDetails1.setTitle(blogHouse5710);

        documentLibPage = ShareUser.createContentInCurrentFolder(drone, contentDetails1, ContentType.PLAINTEXT, documentLibPage);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogTe5710.html", "content blog te1", blogTechno5710);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogTr5710.html", "content blog tr1", blogTrance5710);

        // * news: house, techno, trance
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsH5710.html", "content news h1", newsHouse5710);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsTr5710.html", "content news te1", newsTechno5710);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsTe5710.html", "content news tr1", newsTrance5710);
        // * publications (e.g. rename custom files via Share): house, techno, house techno
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublicationH5710.html", "content publication h1",
                publicationHouse5710);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublicationTe5710.html", "content publication te1",
                publicationTechno5710);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublicationTr5710.html", "content publication tr1",
                publicationTrance5710);

        // Several articles are created in News, Publications, Blogs components:
        // * blogs with content: blog1 with content "house", blog2 with content "techno", blog3 with content "trance"
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogH5711.html", blogHouse5711, "Blog H5711");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogTe5711.html", blogTechno5711, "Blog Te5711");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogTr5711.html", blogTrance5711, "Blog Tr5711");

        // * news articles with content: article1 with content "house", article2 with content "techno", article3 with content "trance"
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsH5711.html", newsHouse5711, "News H5711");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsTe5711.html", newsTechno5711, "News Te5711");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsTr5711.html", newsTrance5711, "News Tr5711");

        // * publication articles with content: publication1 with content "house", publication2 with content "techno", publication3 with content "trance"
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublH5711.html", publicationHouse5711, "Publ H5711");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublTe5711.html", publicationTechno5711, "Publ Te5711");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublTr5711.html", publicationTrance5711, "Publ Tr5711");

        // Several articles are created in News, Publications, Blogs components:
        // * blogs with tags: post1 with tag "house", post2 with tag "techno",post3 with tag "trance"
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogH5712.html", "blog content H5712", "Blog H5712");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogTe5712.html", "blog content Te5712", "Blog Te5712");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "blog", "BlogTr5712.html", "blog content Tr5712", "Blog Tr5712");
        navigateToFolder(documentLibPage, "blog").render();
        documentLibPage.getFileDirectoryInfo("BlogH5712.html").addTag(tagHouse);
        documentLibPage.getFileDirectoryInfo("BlogTe5712.html").addTag(tagTechno);
        documentLibPage.getFileDirectoryInfo("BlogTr5712.html").addTag(tagTrance);

        // * news articles with tags: news1 with tag "house", news2 with tag "techno", news3 with tag "trance"
        ShareUser.openDocumentLibrary(drone);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsH5712.html", "news content H5712", "News H5712");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsTe5712.html", "news content Te5712", "News Te5712");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "news", "NewsTr5712.html", "news content Tr5712", "News Tr5712");
        navigateToFolder(documentLibPage, "news").render();
        documentLibPage.getFileDirectoryInfo("NewsH5712.html").addTag(tagHouse);
        documentLibPage.getFileDirectoryInfo("NewsTe5712.html").addTag(tagTechno);
        documentLibPage.getFileDirectoryInfo("NewsTr5712.html").addTag(tagTrance);

        // * publication articles with tag: paper1 with tag "house", paper2 with tag "techno", paper3 with tag "trance"
        ShareUser.openDocumentLibrary(drone);
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublH5712.html", "publ content H5712", "Publ H5712");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublTe5712.html", "publ content Te5712", "Publ Te5712");
        documentLibPage = navigateToFolderFromRootAndCreateContent(documentLibPage, "publications", "PublTr5712.html", "publ content Tr5712", "Publ Tr5712");
        documentLibPage = navigateToFolder(documentLibPage, "publications").render();
        documentLibPage.getFileDirectoryInfo("PublH5712.html").addTag(tagHouse);
        documentLibPage.getFileDirectoryInfo("PublTe5712.html").addTag(tagTechno);
        documentLibPage.getFileDirectoryInfo("PublTr5712.html").addTag(tagTrance);

        // // setup new entry in hosts to be able to access the new wcmqs site
        // String setHostAddress = "cmd.exe /c echo. >> %WINDIR%\\System32\\Drivers\\Etc\\hosts && echo " + ipAddress + " " + siteName
        // + " >> %WINDIR%\\System32\\Drivers\\Etc\\hosts";
        // Runtime.getRuntime().exec(setHostAddress);
    }

    /*
     * AONE-5708: Search
     */
    @Test(groups = { "WQS", "EnterpriseOnly" })
    public void AONE_5708() throws Exception
    {
        String searchedText = "company";
        int expectedNumberOfSearchedItems = 9;
        // ---- Step 1 ----
        // ---- Step action ----
        // Navigate to http://host:8080/wcmqs
        // ---- Expected results ----
        // Sample site is opened;
        drone.navigateTo(wqsURL);

        // ---- Step 2 ----
        // ---- Step action ----
        // Fill in Search field with company term and click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // Search Results block with the list of found items
        // Latest Blog Articles block
        // Pagination (Next page, Previous page, Page # of #)
        WcmqsHomePage wcmqsHomePage = new WcmqsHomePage(drone);
        wcmqsHomePage.render();
        wcmqsHomePage.searchText(searchedText);

        WcmqsSearchPage wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertTrue(wcmqsSearchPage.verifyNumberOfSearchResultsHeader(expectedNumberOfSearchedItems, searchedText), "The header is not: Showing "
                + expectedNumberOfSearchedItems + " of " + expectedNumberOfSearchedItems + " results for \"" + searchedText + "\" within the website...");
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty.");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertEquals(wcmqsSearchPage.getWcmqsSearchPagePagination(), "Page 1 of 1", "The pagination form is not (Page 1 of 1)");
    }

    /*
     * AONE-5709 Opening blog posts from Search page
     */
    @Test(groups = { "WQS", "EnterpriseOnly" })
    public void AONE_5709() throws Exception
    {
        String searchedText = "company";

        // ---- Step 1 ----
        // ---- Step action ----
        // Navigate to http://host:8080/wcmqs
        // ---- Expected results ----
        // Sample site is opened;

        drone.navigateTo(wqsURL);

        // ---- Step 2 ----
        // ---- Step action ----
        // Fill in Search field with company term and click Search button near Search field;
        // ---- Expected results ----
        // Search page is opened;

        WcmqsHomePage wcmqsHomePage = new WcmqsHomePage(drone);
        wcmqsHomePage.render();
        wcmqsHomePage.searchText(searchedText);

        WcmqsSearchPage wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty.");

        // ---- Step 3 ----
        // ---- Step action ----
        // Click Ethical funds blog post name in Latest Blog Articles section;
        // ---- Expected results ----
        // Blog post is opened successfully;
        wcmqsSearchPage.clickLatestBlogArticle(blogTitle3);
        WcmqsBlogPostPage blogPostPage = new WcmqsBlogPostPage(drone);
        blogPostPage.render();
        Assert.assertEquals(blogPostPage.getTitle(), blogTitle3);

        // ---- Step 4 ----
        // ---- Step action ----
        // Return to Search page and click Company organises workshop blog post name in Latest Blog Articles section;
        // ---- Expected results ----
        // Blog post is opened successfully;
        blogPostPage.getDrone().navigateTo(drone.getPreviousUrl());
        wcmqsSearchPage.render();
        wcmqsSearchPage.clickLatestBlogArticle(blogTitle2);
        blogPostPage = new WcmqsBlogPostPage(drone);
        blogPostPage.render();
        Assert.assertEquals(blogPostPage.getTitle(), blogTitle2);

        // ---- Step 5 ----
        // ---- Step action ----
        // Return to Search page and click Our top analyst's latest thoughts blog post name in Latest Blog Articles section;
        // ---- Expected results ----
        // Blog post is opened successfully;
        blogPostPage.getDrone().navigateTo(drone.getPreviousUrl());
        wcmqsSearchPage.render();
        wcmqsSearchPage.clickLatestBlogArticle(blogTitle1);
        blogPostPage = new WcmqsBlogPostPage(drone);
        blogPostPage.render();
        Assert.assertEquals(blogPostPage.getTitle(), blogTitle1);
    }

    /*
     * AONE-5710 Searching items by name
     */
    @Test(groups = { "WQS", "EnterpriseOnly" })
    public void AONE_5710() throws Exception
    {
        String searchedText = "House";
        String searchedText2 = "techno";
        String searchedText3 = "trance";
        String newsTitle = "House prices face rollercoaster ride";
        String publicationTitle = "Alfresco Datasheet - Social Computing";

        // ---- Step 1 ----
        // ---- Step action ----
        // Navigate to http://host:8080/wcmqs;
        // ---- Expected results ----
        // Sample site is opened;

        drone.navigateTo(wqsURL);

        // ---- Step 2 ----
        // ---- Step action ----
        // Fill in Search field with House term;
        // ---- Expected results ----
        // Data is entered successfully;

        WcmqsHomePage wcmqsHomePage = new WcmqsHomePage(drone);
        wcmqsHomePage.render();
        wcmqsHomePage.searchText(searchedText);

        // ---- Step 3 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // blogs House
        // news article House
        // publication article House
        WcmqsSearchPage wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(blogHouse5710), "Search Results block does not contain title: "
                + blogHouse5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(blogTechno5710), "Search Results block contains title: " + blogTechno5710);
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(newsTitle), "Search Results block does not contain title: " + newsTitle);
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(newsHouse5710), "Search Results block does not contain title: "
                + newsHouse5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(newsTechno5710), "Search Results block contains title: " + newsTechno5710);
        // Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(publicationTitle), "Search Results block does not contain title: "
        // + publicationTitle);
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(publicationHouse5710), "Search Results block does not contain title: "
                + publicationHouse5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(publicationTechno5710), "Search Results block contains title: "
                + publicationTechno5710);

        // ---- Step 4 ----
        // ---- Step action ----
        // Fill in Search field with techno
        // ---- Expected results ----
        // Data is entered successfully;

        wcmqsSearchPage.searchText(searchedText2);

        // ---- Step 5 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // blog Techno
        // news article Techno
        // publication article Techno

        wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(blogTechno5710), "Search Results block does not contain title: "
                + blogTechno5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(blogHouse5710), "Search Results block contains title: " + blogHouse5710);
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(newsTechno5710), "Search Results block does not contain title: "
                + newsTechno5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(newsHouse5710), "Search Results block contains title: " + newsHouse5710);
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(publicationTechno5710), "Search Results block does not contain title: "
                + publicationTechno5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(publicationHouse5710), "Search Results block contains title: "
                + publicationHouse5710);

        // ---- Step 6 ----
        // ---- Step action ----
        // Fill in Search field with trance;
        // ---- Expected results ----
        // Data is entered successfully;

        wcmqsSearchPage.searchText(searchedText3);

        // ---- Step 7 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // blog Trance
        // news article Trance
        // publication article Trance;

        wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(blogTrance5710), "Search Results block does not contain title: "
                + blogTrance5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(blogHouse5710), "Search Results block contains title: " + blogHouse5710);
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(newsTrance5710), "Search Results block does not contain title: "
                + newsTrance5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(newsHouse5710), "Search Results block contains title: " + newsHouse5710);
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains(publicationTrance5710), "Search Results block does not contain title: "
                + publicationTrance5710);
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains(publicationHouse5710), "Search Results block contains title: "
                + publicationHouse5710);

    }

    /*
     * AONE-5711 Searching items by content
     */
    @Test(groups = { "WQS", "EnterpriseOnly" })
    public void AONE_5711() throws Exception
    {
        String searchedText = "House1";
        String searchedText2 = "techno1";
        String searchedText3 = "trance1";

        // ---- Step 1 ----
        // ---- Step action ----
        // Navigate to http://host:8080/wcmqs
        // ---- Expected results ----
        // Sample site is opened;

        drone.navigateTo(wqsURL);

        // ---- Step 2 ----
        // ---- Step action ----
        // Fill in Search field with House term
        // ---- Expected results ----
        // Data is entered successfully;

        WcmqsHomePage wcmqsHomePage = new WcmqsHomePage(drone);
        wcmqsHomePage.render();
        wcmqsHomePage.searchText(searchedText);

        // ---- Step 3 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // blog1
        // article1
        // publication1

        WcmqsSearchPage wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog H5711"), "Search Results block does not contain title: "
                + "Blog H5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog Te5711"), "Search Results block contains title: " + "Blog Te5711");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("News H5711"), "Search Results block does not contain title: "
                + "News H5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("News Te5711"), "Search Results block contains title: " + "News Te5711");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ H5711"), "Search Results block does not contain title: "
                + "Publ H5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ Te5711"), "Search Results block contains title: " + "Publ Te5711");

        // ---- Step 4 ----
        // ---- Step action ----
        // Fill in Search field with techno
        // ---- Expected results ----
        // Data is entered successfully;

        wcmqsSearchPage.searchText(searchedText2);

        // ---- Step 5 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // blog2
        // article2
        // publication2

        wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog Te5711"), "Search Results block does not contain title: "
                + "Blog Te5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog H5711"), "Search Results block contains title: " + "Blog H5711");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("News Te5711"), "Search Results block does not contain title: "
                + "News Te5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("News H5711"), "Search Results block contains title: " + "News H5711");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ Te5711"), "Search Results block does not contain title: "
                + "Publ Te5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ H5711"), "Search Results block contains title: " + "Publ H5711");

        // ---- Step 6 ----
        // ---- Step action ----
        // Fill in Search field with trance;
        // ---- Expected results ----
        // Data is entered successfully;

        wcmqsSearchPage.searchText(searchedText3);

        // ---- Step 7 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // blog3
        // aritcle3
        // publication3

        wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog Tr5711"), "Search Results block does not contain title: "
                + "Blog Tr5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog H5711"), "Search Results block contains title: " + "Blog H5711");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("News Tr5711"), "Search Results block does not contain title: "
                + "News Tr5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("News H5711"), "Search Results block contains title: " + "News H5711");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ Tr5711"), "Search Results block does not contain title: "
                + "Publ Tr5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ H5711"), "Search Results block contains title: " + "Publ H5711");

    }

    /*
     * AONE-5712 Searching items by tags
     */
    @Test(groups = { "WQS", "EnterpriseOnly" })
    public void AONE_5712() throws Exception
    {
        String searchedText = "House2";
        String searchedText2 = "techno2";
        String searchedText3 = "trance2";

        // ---- Step 1 ----
        // ---- Step action ----
        // Navigate to http://host:8080/wcmqs
        // ---- Expected results ----
        // Sample site is opened;

        drone.navigateTo(wqsURL);

        // ---- Step 2 ----
        // ---- Step action ----
        // Fill in Search field with House term
        // ---- Expected results ----
        // Data is entered successfully;

        WcmqsHomePage wcmqsHomePage = new WcmqsHomePage(drone);
        wcmqsHomePage.render();
        wcmqsHomePage.searchText(searchedText);

        // ---- Step 3 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // post1
        // news1
        // paper1

        WcmqsSearchPage wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog H5712"), "Search Results block does not contain title: "
                + "Blog H5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog Te5712"), "Search Results block contains title: " + "Blog Te5712");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("News H5712"), "Search Results block does not contain title: "
                + "News H5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("News Te5712"), "Search Results block contains title: " + "News Te5712");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ H5712"), "Search Results block does not contain title: "
                + "Publ H5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ Te5712"), "Search Results block contains title: " + "Publ Te5712");

        // ---- Step 4 ----
        // ---- Step action ----
        // Fill in Search field with techno
        // ---- Expected results ----
        // Data is entered successfully;

        wcmqsSearchPage.searchText(searchedText2);

        // ---- Step 5 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // post2
        // news2
        // paper2

        wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog Te5712"), "Search Results block does not contain title: "
                + "Blog Te5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog H5712"), "Search Results block contains title: " + "Blog H5712");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("News Te5712"), "Search Results block does not contain title: "
                + "News Te5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("News H5712"), "Search Results block contains title: " + "News H5712");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ Te5712"), "Search Results block does not contain title: "
                + "Publ Te5711");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ H5712"), "Search Results block contains title: " + "Publ H5712");

        // ---- Step 6 ----
        // ---- Step action ----
        // Fill in Search field with trance;
        // ---- Expected results ----
        // Data is entered successfully;

        wcmqsSearchPage.searchText(searchedText3);

        // ---- Step 7 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // The following items are displayed:
        // post3
        // news3
        // paper3

        wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNotNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog Tr5712"), "Search Results block does not contain title: "
                + "Blog Tr5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Blog H5712"), "Search Results block contains title: " + "Blog H5712");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("News Tr5712"), "Search Results block does not contain title: "
                + "News Tr5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("News H5712"), "Search Results block contains title: " + "News H5712");
        Assert.assertTrue(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ Tr5712"), "Search Results block does not contain title: "
                + "Publ Tr5712");
        Assert.assertFalse(wcmqsSearchPage.getTagSearchResults().toString().contains("Publ H5712"), "Search Results block contains title: " + "Publ H5712");

    }

    /*
     * AONE-5714 Empty search
     */
    @Test(groups = { "WQS", "EnterpriseOnly" })
    public void AONE_5714() throws Exception
    {
        String searchedText = "";
        
        // ---- Step 1 ----
        // ---- Step action ----
        // Navigate to http://host:8080/wcmqs
        // ---- Expected results ----
        // Sample site is opened;

        drone.navigateTo(wqsURL);

        // ---- Step 2 ----
        // ---- Step action ----
        // Empty Search field;
        // ---- Expected results ----
        // Data is entered successfully;

        WcmqsHomePage wcmqsHomePage = new WcmqsHomePage(drone);
        wcmqsHomePage.render();
        wcmqsHomePage.searchText(searchedText);

        // ---- Step 3 ----
        // ---- Step action ----
        // Click Search button near Search field;
        // ---- Expected results ----
        // No items are found;

        WcmqsSearchPage wcmqsSearchPage = new WcmqsSearchPage(drone);
        wcmqsSearchPage.render();
        Assert.assertNull(wcmqsSearchPage.getTagSearchResults(), "The Search Results block is not empty");
        Assert.assertTrue(wcmqsSearchPage.isLatestBlogArticlesDisplayed(), "The Latest Blog Articles block is not displayed.");

    }

    private DocumentLibraryPage navigateToFolderFromRootAndCreateContent(DocumentLibraryPage documentLibPage, String folderName, String fileName, String fileContent, String fileTitle)
            throws Exception
    {
        navigateToFolder(documentLibPage, folderName);
        ContentDetails contentDetails1 = new ContentDetails();
        contentDetails1.setName(fileName);
        contentDetails1.setTitle(fileTitle);
        contentDetails1.setContent(fileContent);
        documentLibPage = ShareUser.createContentInCurrentFolder(drone, contentDetails1, ContentType.PLAINTEXT, documentLibPage);
        return documentLibPage;
    }

    private DocumentLibraryPage navigateToFolder(DocumentLibraryPage documentLibPage, String folderName)
    {
        documentLibPage.selectFolder(ALFRESCO_QUICK_START);
        documentLibPage.selectFolder(QUICK_START_EDITORIAL);
        documentLibPage.selectFolder(ROOT);
        documentLibPage.selectFolder(folderName);
        return documentLibPage;
    }

}
