using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.Base.Query
{
    public static class QueryParametersExtensions
    {

        //public static QueryParameters SetParameters(this QueryParameters queryParameters, Dictionary<string, string> dic, bool IsTenant = true)
        //{
        //    //var httpcontext = ServiceLocator.Instance.GetService<IHttpContextAccessor>().HttpContext;
        //    queryParameters.OrderBys = new List<string> { "CreateTime" };
        //    foreach (var p in dic)
        //    {
        //        QueryParameter qp = null;
        //        if (p.Key == "IsDeleted" || p.Key == "Id")
        //        {
        //            qp = new QueryParameter() { FieldName = p.Key, FieldValue = p.Value, ConditionalType = ConditionalType.Equal };

        //        }
        //        else
        //        {
        //            qp = new QueryParameter() { FieldName = p.Key, FieldValue = p.Value };

        //        }
        //        queryParameters.Parameters.Add(qp);
        //    }
        //    if (IsTenant)
        //    {
        //        //if (httpcontext.Request.Headers["TenantLevel"].ToString() == "0")
        //        //{
        //        //    queryParameters.Parameters.Add(new QueryParameter() { ConditionalType = ConditionalType.Equal, FieldName = "TenantId", FieldValue = httpcontext.Request.Headers["TenantId"].ToString() });
        //        //}
        //    }

        //    return queryParameters;
        //}
    }
}