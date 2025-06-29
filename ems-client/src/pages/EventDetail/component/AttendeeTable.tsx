import React from "react";
import type { AttendingUserResponse } from "../../../interface/AttendingUserResponse";

interface Props {
  attendingUserList: AttendingUserResponse[];
}
function AttendeeTable({ attendingUserList }: Props) {
  return (
    <div className="ems-table-wrapper">
      <table className="ems-table">
        <thead>
          <tr>
            <th>#</th>
            {/* <th>User ID</th> */}
            <th>Name</th>
            <th>Email</th>
            <th>Status</th>
            <th>Responded At</th>
          </tr>
        </thead>
        <tbody>
          {attendingUserList?.length > 0 ? (
            <>
              {attendingUserList.map(
                (user: AttendingUserResponse, index: number) => (
                  <tr key={user.userId}>
                    <td>{index + 1}</td>
                    {/* <td>{user.userId}</td> */}
                    <td>{user.name}</td>
                    <td>{user.email}</td>
                    <td>{user.status}</td>
                    <td>{new Date(user.respondedAt).toLocaleString()}</td>
                  </tr>
                )
              )}
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

export default AttendeeTable;
