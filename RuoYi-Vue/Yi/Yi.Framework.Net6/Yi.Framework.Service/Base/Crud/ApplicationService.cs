using AutoMapper;
using AutoMapper.Internal.Mappers;
using Microsoft.Extensions.DependencyInjection;
using System;
using Yi.Framework.Common.Attribute;
using Yi.Framework.Interface.Base.Crud;

namespace Yi.Framework.Service.Base.Crud
{
    public class ApplicationService : IApplicationService
    {
        [Autowired]
        public IServiceProvider ServiceProvider { get; set; }
        protected IMapper ObjectMapper => ServiceProvider.GetRequiredService<IMapper>();
    }
}