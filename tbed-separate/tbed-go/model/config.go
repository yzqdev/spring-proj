package model

type Config struct {
	Id          int    `json:"id"`
	Sourcekey   int    `json:"sourcekey"`
	Emails      int    `json:"emails"`
	Webname     string `json:"webname"`
	Explain     string `json:"explain"`
	Video       string `json:"video"`
	Links       string `json:"links"`
	Notice      string `json:"notice"`
	Baidu       string `json:"baidu"`
	Backtype    string `json:"backtype"`
	Domain      string `json:"domain"`
	Background1 string `json:"background1"`
	Background2 string `json:"background2"`
	Webms       string `json:"webms"`
	Webkeywords string `json:"webkeywords"`
	Webfavicons string `json:"webfavicons"`
	Websubtitle string `json:"websubtitle"`
	Logo        string `json:"logo"`
	AboutInfo   string `json:"aboutInfo"`
}
