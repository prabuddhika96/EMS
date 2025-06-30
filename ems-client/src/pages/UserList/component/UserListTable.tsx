import React from "react";
import type { User } from "../../../interface/User";
import { useSelector } from "react-redux";
import type { RootState } from "../../../redux/store";

interface Props {
  userList: User[];
  changeUserRole: any;
}
function UserListTable({ userList, changeUserRole }: Props) {
  const loggedUser: User = useSelector((state: RootState) => state.user);
  return (
    <div className="ems-table-wrapper">
      <table className="ems-table">
        <thead>
          <tr>
            <th>#</th>
            <th>User ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Status</th>
            <th>Make As Admin</th>
          </tr>
        </thead>
        <tbody>
          {userList?.length > 0 ? (
            <>
              {userList.map((user: User, index: number) => (
                <tr key={user.id}>
                  <td>{index + 1}</td>
                  <td>{user.id}</td>
                  <td>{user.name}</td>
                  <td>{user.email}</td>
                  <td>{user.role}</td>
                  <td>
                    <select
                      value={user.role ?? "USER"}
                      onChange={(e) => {
                        changeUserRole(user, e.target.value);
                      }}
                      className="user-list-select"
                      disabled={loggedUser?.id == user?.id}
                    >
                      <option value="ADMIN">ADMIN</option>
                      <option value="USER">USER</option>
                    </select>
                  </td>
                </tr>
              ))}
            </>
          ) : (
            <tr>
              <td colSpan={5} style={{ textAlign: "center" }}>
                No Data Found
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default UserListTable;
