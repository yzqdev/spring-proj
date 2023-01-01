package model

type UserGroup struct {
	Id      uint   `json:"id"  gorm:"primaryKey"`
	UserId  string `json:"user_id"`
	GroupId string `json:"group_id"`
}
