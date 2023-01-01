package main

import (
	"github.com/gin-gonic/gin"
	"tbed-go/controller"
	"tbed-go/middleware"
)

func InitRouter(e *gin.Engine) {
	baseRouter := e.Group("/auth")
	{
		baseRouter.POST("/login", controller.Login)
		baseRouter.POST("/reg", controller.Register)
	}
	pageRouter := e.Group("/")
	{
		pageRouter.GET("/", controller.IndexPage)
	}
	adminRouter := e.Group("/admin", middleware.JwtHandler())
	{

		adminRouter.GET("/index", controller.Index)
		adminRouter.GET("/checkToken", controller.CheckToken)

	}
	homeRouter := e.Group("/default")
	{
		homeRouter.GET("/index")

	}

}
