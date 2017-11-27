<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:text="http://www.ora.com/XSLTCookbook/namespaces/text"
                xmlns:ls="http://ohapegor.ru/logFinder/services/webServices/soap/schema">
    <xsl:include href="text.wrap.xslt"/>
    <xsl:strip-space elements="*"/>
    <xsl:output method="text"/>


    <xsl:variable name="doc_width" select="150"/>


    <xsl:template match="ls:searchInfoResult">
        <!--таблица searchInfo-->
        <!--заголовки-->
        <xsl:apply-templates select="ls:searchInfo"/>
        <xsl:text>&#xa;</xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'Приложение logFinder' "/>
            <xsl:with-param name="width" select="$doc_width"/>
            <xsl:with-param name="align" select=" 'center' "/>
        </xsl:call-template>
        <xsl:text>&#xa;</xsl:text>
        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'Создатель: Охапкин Егор, ООО «Siblion»' "/>
            <xsl:with-param name="width" select="$doc_width"/>
            <xsl:with-param name="align" select=" 'center' "/>
        </xsl:call-template>
        <xsl:text>&#xa;&#xa;</xsl:text>

        <xsl:variable name="logCount" select="count(ls:resultLogs)"/>
        <xsl:if test="$logCount = 0">
            <xsl:call-template name="text:justify">
                <xsl:with-param name="value" select="ls:emptyResultMessage"/>
                <xsl:with-param name="width" select="$doc_width"/>
                <xsl:with-param name="align" select=" 'center' "/>
            </xsl:call-template>
            <xsl:text>&#xa;&#xa;</xsl:text>
        </xsl:if>

        <xsl:for-each select="ls:resultLogs">
            <xsl:text>Log №</xsl:text>
            <xsl:number count="ls:resultLogs" format="1. "/>
            <xsl:text>&#xa;</xsl:text>
            <xsl:text>Time moment : </xsl:text><xsl:value-of select="ls:timeMoment"/><xsl:text>&#xa;</xsl:text>
            <xsl:text>File : </xsl:text><xsl:value-of select="ls:fileName"/><xsl:text>&#xa;</xsl:text>
            <xsl:apply-templates select="ls:content"/>
        </xsl:for-each>
    </xsl:template>


    <xsl:template match="ls:content">
        <xsl:apply-templates select="." mode="text:wrap">
            <xsl:with-param name="width" select="$doc_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
            <xsl:with-param name="align-width" select="$doc_width"/>
        </xsl:apply-templates>
        <xsl:text>&#xa;</xsl:text>
    </xsl:template>

    <!--таблица searchInfo-->
    <xsl:template match="ls:searchInfo">

        <xsl:variable name="info_table_width" select="77"/>
        <xsl:variable name="column_width" select="35"/>

        <!--заполнение пробелами на ширину width-->
        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>

        <!--заголовок таблицы-->
        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'Параметры поиска:' "/>
            <xsl:with-param name="width" select="$info_table_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
        </xsl:call-template>
        <xsl:text>&#xa;</xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text> --------------------------------------------------------------------------- &#xa;</xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text>| </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'Регулярное выражение' "/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
        </xsl:call-template>
        <xsl:text> | </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select="ls:regexp"/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
        </xsl:call-template>
        <xsl:text> |&#xa;</xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text>| </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'Расположение логов' "/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
        </xsl:call-template>
        <xsl:text> | </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select="concat(ls:location,'')"/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
        </xsl:call-template>
        <xsl:text> |&#xa;</xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text> --------------------------------------------------------------------------- &#xa;</xsl:text>

        <!--временные интервалы поиска-->
        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text>| </xsl:text>


        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'Временные промежутки' "/>
            <xsl:with-param name="width" select="($info_table_width - 4)"/>
            <xsl:with-param name="align" select=" 'center' "/>
        </xsl:call-template>
        <xsl:text> |&#xa;</xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text>| </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'dateFrom' "/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'center' "/>
        </xsl:call-template>
        <xsl:text> | </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" 'dateTo' "/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'center' "/>
        </xsl:call-template>
        <xsl:text> |&#xa;</xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text> --------------------------------------------------------------------------- &#xa;</xsl:text>


        <xsl:for-each select="ls:dateIntervals">
        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select=" '' "/>
            <xsl:with-param name="width" select="($doc_width - $info_table_width)"/>
            <xsl:with-param name="align" select=" 'right' "/>
        </xsl:call-template>
        <xsl:text>| </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select="ls:dateFrom"/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
        </xsl:call-template>
        <xsl:text> | </xsl:text>

        <xsl:call-template name="text:justify">
            <xsl:with-param name="value" select="ls:dateTo"/>
            <xsl:with-param name="width" select="$column_width"/>
            <xsl:with-param name="align" select=" 'left' "/>
        </xsl:call-template>
        <xsl:text> |&#xa;</xsl:text>

        </xsl:for-each>

    </xsl:template>


</xsl:stylesheet>