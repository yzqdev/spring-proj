using SqlSugar;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.RABC
{
    public partial class FileService : BaseService<FileEntity>, IFileService
    {
        public FileService(IRepository<FileEntity> repository) : base(repository)
        {
        }
    }
}
