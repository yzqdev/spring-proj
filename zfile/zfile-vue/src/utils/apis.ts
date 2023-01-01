import http from "./http";
import qs from "qs";
export const resetPassApi = () => {
  return http.get("/debug/resetPwd");
};
export const loginApi = (data: any) => {
  console.log(http.defaults.withCredentials);
  return http.post("/doLogin", data);
};
export const isInstalled = () => {
  return http.get("/is-installed");
};
export const logoutApi = () => {
  return http.get("/logout");
};
export const adminConfigApi = (data: any) => {
  return http.post("/admin/config", data);
};
export const getAdminConfigApi = () => {
  return http.get("/admin/config");
};
export const updatePassApi = (data: any) => {
  return http.post("/admin/update-pwd", data);
};
export const linkListApi = (data: any) => {
  return http.get("/admin/link/list", { params: data });
};
export const getDrivesApi = () => {
  return http.get("/api/drive/list");
};
export const getSiteApi = () => {
  return http.get("/sharepoint/getSiteId");
};
export const getStorageForm = (storageType: string) => {
  return http.get(`/admin/strategy-form?storageType=${storageType}`);
};
