using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Common.Helper;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 用户表
    ///</summary>
    [SugarTable("User")]
    public partial class UserEntity : IBaseModelEntity
    {
        public UserEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Name")]
        public string? Name { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Age")]
        public int? Age { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "CreateUser")]
        public long? CreateUser { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "CreateTime")]
        public DateTime? CreateTime { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "ModifyUser")]
        public long? ModifyUser { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "ModifyTime")]
        public DateTime? ModifyTime { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "IsDeleted")]
        public bool? IsDeleted { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "TenantId")]
        public long? TenantId { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "UserName")]
        public string? UserName { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Password")]
        public string? Password { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Salt")]
        public string? Salt { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Icon")]
        public string? Icon { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Nick")]
        public string? Nick { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Email")]
        public string? Email { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Ip")]
        public string? Ip { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Address")]
        public string? Address { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Phone")]
        public string? Phone { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "Introduction")]
        public string? Introduction { get; set; }
        /// <summary>
        /// 排序字段 
        ///</summary>
        [SugarColumn(ColumnName = "OrderNum")]
        public int? OrderNum { get; set; }
        /// <summary>
        /// 描述 
        ///</summary>
        [SugarColumn(ColumnName = "Remark")]
        public string? Remark { get; set; }
        /// <summary>
        /// 部门id 
        ///</summary>
        [SugarColumn(ColumnName = "DeptId")]
        public long? DeptId { get; set; }
        /// <summary>
        /// 性别 
        ///</summary>
        [SugarColumn(ColumnName = "Sex")]
        public int? Sex { get; set; }
        /// <summary>
        ///  看好啦！ORM精髓，导航属性
        ///</summary>
        [Navigate(typeof(UserRoleEntity), nameof(UserRoleEntity.UserId), nameof(UserRoleEntity.RoleId))]
        public List<RoleEntity>? Roles { get; set; }

        [Navigate(typeof(UserPostEntity), nameof(UserPostEntity.UserId), nameof(UserPostEntity.PostId))]
        public List<PostEntity>? Posts { get; set; }

        [Navigate(NavigateType.OneToOne, nameof(DeptId))]
        public DeptEntity? Dept { get; set; }

        /// <summary>
        /// 构建密码，MD5盐值加密
        /// </summary>
        public UserEntity BuildPassword(string? password = null)
        {
            //如果不传值，那就把自己的password当作传进来的password
            if (password == null)
            {
                if (Password == null)
                {
                    throw new ArgumentNullException("Password不能为空");
                }
                password = Password;
            }
            Salt = MD5Helper.GenerateSalt();
            Password = MD5Helper.SHA2Encode(password, Salt);
            return this;
        }

        /// <summary>
        /// 判断密码和加密后的密码是否相同
        /// </summary>
        /// <param name="password"></param>
        /// <returns></returns>
        public bool JudgePassword(string password)
        {
            if (this.Salt is  null)
            {
                throw new ArgumentNullException(this.Salt);
            }
            var p = MD5Helper.SHA2Encode(password, Salt);
            if (Password == MD5Helper.SHA2Encode(password, Salt))
            {
                return true;
            }
            return false;
        }
    }
}
