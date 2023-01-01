import http from "./http";

export const driveUpdateIdApi = (data: any) => {
  return http.post("/admin/drive/updateId", data);
};
export const driveDragApi = (data: any) => {
  return http.post("/admin/drive/drag", data);
};
export const cacheCleanApi = (currentCacheManageId: any) => {
  return http.post(`/admin/cache/${currentCacheManageId}/clear`);
};
export const driveEnableApi = (id: any, action: string) => {
  return http.post(`/admin/drive/${id}/${action}`);
};
export const getDriveFilterApi = (driveId: string) => {
  return http.get(`/admin/drive/${driveId}/filters`);
};
export const postDriveFilterApi = (driveId: string, list: any) => {
  return http.post(`/admin/drive/${driveId}/filters`, list);
};
export const CacheApi = (id: any, action: string) => {
  return http.post(`/admin/cache/${id}/${action}`);
};
export const CacheFreshApi = (id: any, action: string) => {
  return http.post(`/admin/cache/${id}/auto-refresh/${action}`);
};
export const cacheInfoApi = (currentCacheManageId: any) => {
  return http.get(`/admin/cache/${currentCacheManageId}/info`);
};
export const refreshCacheApi = (currentCacheManageId: any, data: any) => {
  return http.get(`/admin/cache/${currentCacheManageId}/refresh`, data);
};
export const cacheClearApi = (currentCacheManageId: any) => {
  return http.post(`/admin/cache/${currentCacheManageId}/clear`);
};
export const getDriveApi = (id: any) => {
  return http.get("/admin/drive/" + id);
};
export const delDriveApi = (id: any) => {
  return http.delete("/admin/drive/" + id);
};
export const strategyApi = () => {
  return http.get("/admin/support-strategy");
};
