package model

type EmailConfig struct {
	Id        uint   `json:"id" gorm:"primaryKey"`
	Emails    string `json:"emails"`
	EmailKey  string `json:"email_key"`
	EmailUrl  string `json:"email_url"`
	Port      string `json:"port"`
	EmailName string `json:"email_name"`
	Using     string `json:"using"`
}
