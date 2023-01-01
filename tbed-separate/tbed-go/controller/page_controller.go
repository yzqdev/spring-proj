package controller

import (
	"github.com/gin-gonic/gin"
	"tbed-go/utils"
)

func IndexPage(c *gin.Context) {
	utils.JSON(c, 200, "success", "index page")

}
