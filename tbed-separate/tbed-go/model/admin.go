package model

import (
	"github.com/gookit/color"
	"github.com/rs/xid"
	"time"
)

type AdminUser struct {
	ID         uint      `json:"id" gorm:"primaryKey"`
	Username   string    `json:"username"`
	Password   string    `json:"password"`
	Email      string    `json:"email,omitempty"`
	Birthday   time.Time `json:"birthday,omitempty" gorm:"default:null;type:timestamp"`
	Level      uint      `json:"level"`
	Uid        xid.ID    `json:"uid"  gorm:"primaryKey"`
	Isok       bool      `json:"isok"`
	Memory     string    `json:"memory"`
	GroupId    uint      `json:"group_id,omitempty"`
	CreateTime time.Time `json:"create_time"`
	UpdateTime time.Time `json:"update_time"`
}

func QueryByUsername(username string) (result AdminUser) {

	db := GetDb()
	db.Table("user").Where("username = ?", username).First(&result)
	color.Red.Println(result)
	return

}
func SaveUser(data *AdminUser) {

	db := GetDb()
	db.Table("user").Create(data)

}
func GetUserCheck(username string) bool {
	db := GetDb()
	obj := db.Table("user").
		Where("username = ?  ", username).
		Where("status in (?)", []string{"1", "2"})
	var count int64
	obj.Count(&count)

	if count > 0 {
		return true
	} else {
		return false
	}
}
func QueryCheckToken() {

}
