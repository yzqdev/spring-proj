//深拷贝
export function deepCopy(obj) {
    var a = JSON.stringify(obj);
    var newobj = JSON.parse(a);
    return newobj;
}


//转换数据,0是相等，1是模糊查询
export function objctToDic(object, isByPage) {
    if (object == undefined) {

        object = {};
    }

    if (isByPage) {
        var paramPage = {
            "index": object.pageIndex,
            "size": object.pageSize,
            "parameters": [],
            "orderBys": []
        }

        var newData = deepCopy(object);
        delete newData.pageIndex;
        delete newData.pageSize;

        var newList = [Object.keys(newData).map(val => {
            return {
                key: val,
                value: object[val],
                type: 1
            }
        })]

        //过滤封装
        newList[0].forEach((item, index) => {
            if (item.value.length > 0) {
                if (item.key == 'isDeleted') {
                    item.type = 0;
                }
                paramPage.parameters.push(item);
            }
        });

        return paramPage;
    }
    else {
        var params = {
            "parameters": [],
            "orderBys": []
        }
        var thisList = [Object.keys(object).map(val => {
            return {
                key: val,
                value: object[val],
                type: 1
            }
        })]
        thisList[0].forEach((item, index) => {
            if (item.value.length > 0) {
                if (item.key == 'isDeleted') {
                    item.type = 0;
                }
                params.parameters.push(item);
            }
        });

        return params;
    }

} 