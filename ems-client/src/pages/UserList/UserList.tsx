import React, { useEffect, useState } from "react";
import type { User } from "../../interface/User";
import { userService } from "../../service/userSevice";
import HeaderH2 from "../../components/Headers/HeaderH2/HeaderH2";
import UserListTable from "./component/UserListTable";
import "./style.css";
import PaginationComponent from "../../components/Pagination/PaginationComponent";
import PageSizeComponent from "../../components/PageSizeSelector/PageSizeComponent";
import type { IAlert } from "../../interface/Alert";
import Alert from "../../components/Alert/Alert";

interface Response {
  userList: User[];
  totalPages: number;
  totalRecords: number;
}

const initialState: Response = {
  userList: [],
  totalPages: 1,
  totalRecords: 0,
};

function UserList() {
  const [responseData, setResponseData] = useState<Response>(initialState);
  const [page, setPage] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);
  const [roleUpdateAlert, setRoleUpdateAlert] = useState<IAlert | null>(null);

  const fetchUserList = async (page: number, pageSize: number) => {
    try {
      const apiResponse: any = await userService.getUserList(page, pageSize);

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code == 2004) {
          // console.log(apiResponse?.data?.data?.content);
          setResponseData({
            userList: apiResponse?.data?.data?.content,
            totalPages: apiResponse?.data?.data?.totalPages || 1,
            totalRecords: apiResponse?.data?.data?.totalElements || 0,
          });
        }
      }
    } catch (error) {}
  };

  useEffect(() => {
    fetchUserList(page, pageSize);
  }, []);

  const handlePageChange = (value: number) => {
    if (value == page) {
      return;
    }
    setPage(Number(value));
    fetchUserList(Number(value), pageSize);
  };

  const handleChangePageSize = (value: string) => {
    setPageSize(Number(value));
    setPage(1);
    fetchUserList(1, Number(value));
  };

  const changeUserRole = async (user: User, role: "ADMIN" | "USER") => {
    setRoleUpdateAlert(null);
    try {
      // debugger;
      if (user?.id == null || user.role == role) {
        return;
      }
      const apiResponse: any = await userService.updateUserRole(user.id, role);

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
        setRoleUpdateAlert({
          message:
            apiResponse?.message?.toString() || "An unknown error occurred.",
          type: "error",
        });
      } else {
        if (apiResponse?.data?.code == 2006) {
          const index = responseData?.userList?.findIndex(
            (userData: User) => userData?.id === user?.id
          );

          if (index !== -1) {
            const updatedUserList = [...responseData.userList];
            updatedUserList[index] = {
              ...updatedUserList[index],
              role: role,
            };

            setResponseData((prev) => ({
              ...prev,
              userList: updatedUserList,
            }));

            setRoleUpdateAlert({
              message: apiResponse?.data?.message || "User role changed",
              type: "success",
            });
          }
        } else {
          setRoleUpdateAlert({
            message: apiResponse?.data?.message || "User role changed",
            type: "success",
          });
        }
      }
    } catch (error) {}
  };

  return (
    <div className="user-list-container">
      <HeaderH2 text={"User List"} />

      {roleUpdateAlert && <Alert alert={roleUpdateAlert} />}

      <div className="user-table-container">
        <UserListTable
          userList={responseData?.userList ?? []}
          changeUserRole={changeUserRole}
        />
      </div>

      {responseData?.userList && responseData.userList?.length > 0 && (
        <>
          <div className="pagination-row">
            <PaginationComponent
              totalPages={responseData?.totalPages}
              currentPage={page}
              onPageChnage={handlePageChange}
            />
            <PageSizeComponent
              totalRecords={responseData?.totalRecords}
              handleChangePageSize={handleChangePageSize}
            />
          </div>
        </>
      )}
    </div>
  );
}

export default UserList;
