package model

import "time"

type T struct {
	AlbumKey   string    `json:"albumKey"`
	AlbumTitle string    `json:"albumTitle"`
	CreateTime string    `json:"createTime"`
	Password   string    `json:"password"`
	UpdateTime time.Time `json:"updateTime"`
	UserId     int       `json:"userId"`
	Username   string    `json:"username"`
}
