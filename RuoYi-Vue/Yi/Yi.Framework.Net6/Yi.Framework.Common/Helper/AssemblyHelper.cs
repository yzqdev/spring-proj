using AutoMapper;
using AutoMapper.Internal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Helper
{
    public class AssemblyHelper
    {
        public static List<Type> GetClass(string assemblyFile, string? className = null, string? spaceName = null)
        {
            Assembly assembly = Assembly.Load(assemblyFile);
            return assembly.GetTypes().Where(m => m.IsClass
            && className == null ? true : m.Name == className
            && spaceName == null ? true : m.Namespace == spaceName
             ).ToList();
        }

        public static List<Type> GetClassByBaseClassesAndInterfaces(string assemblyFile, Type type)
        {
            Assembly assembly = Assembly.Load(assemblyFile);

            List<Type> resList = new List<Type>();

            List<Type> typeList = assembly.GetTypes().Where(m => m.IsClass).ToList();
            foreach (var t in typeList)
            {
                var data = t.BaseClassesAndInterfaces();
                if (data.Contains(type))
                {
                    resList.Add(t);
                }

            }
            return resList;
        }
    }
}