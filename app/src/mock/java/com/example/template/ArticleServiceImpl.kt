package com.example.template

import com.example.template.home.data.remote.ArticleDataSource
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.servicemodels.Article
import kotlinx.coroutines.delay

class ArticleServiceImpl(val api: HomeApi) : ArticleDataSource {

    private fun returnResponse(responseIsSuccess: Boolean): List<Article> {
        return if (responseIsSuccess) {
            returnSuccessResponse()
        } else {
            returnErrorResponse()
        }
    }

    private fun returnSuccessResponse(): List<Article> {
        return mutableListOf(
            Article(
                "1",
                "dini",
                "darse dini ziba nistziba nistziba nistziba nistziba nist ",
                "mamd",
                13,
                true
            ),
            Article(
                "2",
                "oloom",
                "darse oloom ziba nistziba nistziba nistziba nistziba nist  ",
                "reaq",
                14,
                false
            ),
            Article(
                "3",
                "math",
                "darse math ziba nist ziba nist ziba nist ziba nist ziba nist  ",
                "hosein",
                14,
                true
            ),

            Article(
                "4",
                "chemistry",
                "darse chemistry ziba chemistry  ziba chemistry  ziba chemistry  ziba chemistry  ziba chemistry  ",
                "asghar",
                15,
                true
            ),

            Article(
                "5",
                "hendese",
                "darse hendese ziba hendese  ziba hendese  ziba hendese  ziba hendese  ",
                "kamran",
                16,
                false
            ),

            Article(
                "6",
                "salamat va behdasht",
                "darse salamat va behdasht darse salamat va behdasht darse salamat va behdasht darse salamat va behdasht darse dini ",
                "amir",
                16,
                true
            ),

            Article(
                "7",
                "moadelat diff",
                "darse moadelat diff darse moadelat diff darse moadelat diff darse moadelat diff darse dini ",
                "sajad",
                17,
                false
            ),

            Article(
                "8",
                "falsafe",
                "darse falsafe dinidarse falsafe falsafe dinidarse falsafe dini ",
                "zahra",
                18,
                true
            ),

            Article(
                "9",
                "eghtesad",
                "darse eghtesad dinidarse eghtesad dinidarse eghtesad dinidarse eghtesad ",
                "fateme",
                19,
                false
            ),

            Article(
                "10",
                "zist shenasi",
                "darse zist shenasi darse zist shenasi darsezist shenasidarse zist shenasi darse dini darse dini ",
                "nadia",
                20,
                true
            ),

            Article(
                "11",
                "microb shenasi",
                "darse microb shenasi dinidarse microb shenasi dinidarse microb shenasi ",
                "hashem",
                21,
                false
            ),

            Article(
                "12",
                "mavvad hajeb",
                "darse mavvad hajeb dinidarse mavvad hajeb dmavvad hajebarse dinidarse dinidarse dini ",
                "javad",
                22,
                true
            ),

            Article(
                "13",
                "computer",
                "darse computer darse computer darse computer darse computerdarse dini darse dini darse dini darse dini ",
                "amin",
                22,
                false
            ),

            Article(
                "14",
                "madar manteghi",
                "darsemadar manteghidarsemadar manteghi darsemadar manteghi darse dini darse dini darse dini darse dini darse dini ",
                "kobra",
                23,
                true
            ),

            Article(
                "15",
                "riz pardazande",
                "darse riz pardazande darse riz pardazande darseriz pardazandedarse dini darse dini darse dini darse dini darse dini darse dini ",
                "soosan",
                24,
                false
            ),

            Article(
                "16",
                "memary",
                "darse memary darse memary darse dimemaryni darse dini darse dini darse dini darse dini darse dini ",
                "narges",
                25,
                true
            ),

            Article(
                "17",
                "physic",
                "darse physic darse physic darse physic darse dini darse dini darse dini darse dini darse dini darse dini ",
                "behnoosh",
                26,
                false
            ),


            )
    }

    private fun returnErrorResponse(): List<Article> {
        throw Exception("error")
    }


    override suspend fun getArticle(userId: Int): List<Article> {
        delay(3000L)
        return returnResponse(true)
    }
}