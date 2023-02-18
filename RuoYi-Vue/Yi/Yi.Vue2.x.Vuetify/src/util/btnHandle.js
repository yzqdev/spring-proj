import store from '../store/index'
//匹配按钮，判断是否有按钮存在

function getBtn(par) {
    var axiosEnable = {
        get: false,
        update: false,
        del: false,
        add: false,
    };
    const per = store.state.user.per;
    switch (par) {
        case "user":
            per.forEach(p => {
                if(p=="user:get")
                {
                    axiosEnable.get=true;
                }
                else if(p=="user:update")
                {
                    axiosEnable.update=true;
                }
                else if(p=="user:del")
                {
                    axiosEnable.del=true;
                }
                else if(p=="user:add")
                {
                    axiosEnable.add=true;
                }
            });
            break;
        case "role":
            per.forEach(p => {
            if(p=="role:get")
            {
                axiosEnable.get=true;
            }
            else if(p=="role:update")
            {
                axiosEnable.update=true;
            }
            else if(p=="role:del")
            {
                axiosEnable.del=true;
            }
            else if(p=="role:add")
            {
                axiosEnable.add=true;
            }   
        }) 
        break;
        case "menu":
            per.forEach(p => {
            if(p=="menu:get")
            {
                axiosEnable.get=true;
            }
            else if(p=="menu:update")
            {
                axiosEnable.update=true;
            }
            else if(p=="menu:del")
            {
                axiosEnable.del=true;
            }
            else if(p=="menu:add")
            {
                axiosEnable.add=true;
            }    
        })
        break;


        default: console.log("未发现合法路由")
    }
    return axiosEnable;
}


export  {getBtn};
