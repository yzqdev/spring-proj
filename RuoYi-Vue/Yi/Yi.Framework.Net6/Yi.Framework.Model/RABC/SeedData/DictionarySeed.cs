using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.Model.RABC.SeedData
{
    public class DictionarySeed : AbstractSeed<DictionaryEntity>
    {
        public override List<DictionaryEntity> GetSeed()
        {
            DictionaryEntity dict1 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "用户性别",
                DictType = "sys_user_sex",
                OrderNum = 100,
                Remark = "用户性别列表",
                IsDeleted = false,
            };
            Entitys.Add(dict1);

            DictionaryEntity dict2 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "菜单状态",
                DictType = "sys_show_hide",
                OrderNum = 100,
                Remark = "菜单状态列表",
                IsDeleted = false,
            };
            Entitys.Add(dict2);

            DictionaryEntity dict3 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "系统开关",
                DictType = "sys_normal_disable",
                OrderNum = 100,
                Remark = "系统开关列表",
                IsDeleted = false,
            };
            Entitys.Add(dict3);

            DictionaryEntity dict4 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "任务状态",
                DictType = "sys_job_status",
                OrderNum = 100,
                Remark = "任务状态列表",
                IsDeleted = false,
            };
            Entitys.Add(dict4);

            DictionaryEntity dict5 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "任务分组",
                DictType = "sys_job_group",
                OrderNum = 100,
                Remark = "任务分组列表",
                IsDeleted = false,
            };
            Entitys.Add(dict5);

            DictionaryEntity dict6 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "系统是否",
                DictType = "sys_yes_no",
                OrderNum = 100,
                Remark = "系统是否列表",
                IsDeleted = false,
            };
            Entitys.Add(dict6);

            DictionaryEntity dict7 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "通知类型",
                DictType = "sys_notice_type",
                OrderNum = 100,
                Remark = "通知类型列表",
                IsDeleted = false,
            };
            Entitys.Add(dict7);
            DictionaryEntity dict8 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "通知状态",
                DictType = "sys_notice_status",
                OrderNum = 100,
                Remark = "通知状态列表",
                IsDeleted = false,
            };
            Entitys.Add(dict8);

            DictionaryEntity dict9 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "操作类型",
                DictType = "sys_oper_type",
                OrderNum = 100,
                Remark = "操作类型列表",
                IsDeleted = false,
            };
            Entitys.Add(dict9);


            DictionaryEntity dict10 = new DictionaryEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DictName = "系统状态",
                DictType = "sys_common_status",
                OrderNum = 100,
                Remark = "登录状态列表",
                IsDeleted = false,
            };
            Entitys.Add(dict10);
            return Entitys;
        }
    }
}
