import React, { useEffect, useState } from "react";
import type { CreateEventForm } from "../../interface/Form";
import "./style.css";
import { validateCreateEventForm } from "../../util/validation";
import { eventService } from "../../service/eventService";
import type { IAlert } from "../../interface/Alert";
import Alert from "../../components/Alert/Alert";
import { convertToCleanUTCISOString } from "../../util/time-utils";
import { useParams } from "react-router-dom";

export const initialState: CreateEventForm = {
  title: "",
  description: "",
  startTime: "",
  endTime: "",
  location: "",
  visibility: null,
};

function CreateEvent() {
  const { id }: any = useParams();
  const [formData, setFormData] = useState<CreateEventForm>(initialState);
  const [errors, setErrors] = useState<CreateEventForm>(initialState);
  const [alert, setAlert] = useState<IAlert | null>(null);

  const fetchEvent = async (eventId: string) => {
    try {
      const apiResponse: any = await eventService.getEventById(eventId);

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code === 3003) {
          setFormData(apiResponse?.data?.data);
        }
      }
    } catch (error) {
      console.error("Unexpected error:", error);
    }
  };

  useEffect(() => {
    if (id) {
      fetchEvent(id);
    }
  }, [id]);

  const handleChnage = (name: keyof CreateEventForm, value: string) => {
    console.log(name, value);
    setFormData((prev: CreateEventForm) => ({
      ...prev,
      [name]: value,
    }));

    setErrors((prev: CreateEventForm) => ({
      ...prev,
      [name]: "",
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setAlert(null);
    setErrors(initialState);
    const hasError: boolean = validateCreateEventForm(formData, setErrors);

    if (hasError) {
      return;
    }

    try {
      const payload = {
        ...formData,
        startTime: convertToCleanUTCISOString(formData.startTime),
        endTime: convertToCleanUTCISOString(formData.endTime),
        visibility:
          formData.visibility == null ? "PUBLIC" : formData.visibility,
      };
      let apiResponse: any = null;
      if (id) {
        apiResponse = await eventService.updateEvent(payload, id);
      } else {
        apiResponse = await eventService.createEvent(payload);
      }
      if (apiResponse == null) {
        setAlert({
          message: "An unknown error occurred.",
          type: "error",
        });
        return;
      }
      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
        setAlert({
          message:
            apiResponse?.message?.toString() || "An unknown error occurred.",
          type: "error",
        });
      } else {
        // console.log(apiResponse?.data);
        if (apiResponse?.data?.code == 3000) {
          setAlert({
            message: apiResponse?.data?.message || "Event created successfully",
            type: "success",
          });
        } else if (apiResponse?.data?.code == 3001) {
          setAlert({
            message: apiResponse?.data?.message || "Event updated successfully",
            type: "success",
          });
        }
      }
    } catch (error) {}
  };

  return (
    <div>
      <div className="create-event-container">
        <h2> {id ? `Update Event` : `Create Event`}</h2>
        {alert && <Alert alert={alert} />}

        <form onSubmit={handleSubmit}>
          {/* Title */}
          <div className="create-event-field">
            <label>Title</label>
            <input
              type="text"
              value={formData.title}
              onChange={(e) => handleChnage("title", e.target.value)}
            />
            <p className="error-text">{errors.title}</p>
          </div>

          {/* Description */}
          <div className="create-event-field">
            <label>Description</label>
            <textarea
              value={formData.description}
              onChange={(e) => handleChnage("description", e.target.value)}
            />
            <p className="error-text">{errors.description}</p>
          </div>

          <div className="create-event-grid">
            {/* Start Time */}
            <div className="create-event-field">
              <label>Start Time</label>
              <input
                type="datetime-local"
                value={formData.startTime}
                onChange={(e) => handleChnage("startTime", e.target.value)}
              />
              <p className="error-text">{errors.startTime}</p>
            </div>

            {/* End Time */}
            <div className="create-event-field">
              <label>End Time</label>
              <input
                type="datetime-local"
                value={formData.endTime}
                onChange={(e) => handleChnage("endTime", e.target.value)}
              />
              <p className="error-text">{errors.endTime}</p>
            </div>
          </div>

          <div className="create-event-grid">
            {/* Location */}
            <div className="create-event-field">
              <label>Location</label>
              <input
                type="text"
                value={formData.location}
                onChange={(e) => handleChnage("location", e.target.value)}
              />
              <p className="error-text">{errors.location}</p>
            </div>

            {/* Visibility */}
            <div className="create-event-field">
              <label>Visibility</label>
              <select
                value={formData.visibility ?? ""}
                onChange={(e) => handleChnage("visibility", e.target.value)}
              >
                <option value="" disabled selected>
                  Select Visibility
                </option>
                <option value="PUBLIC">Public</option>
                <option value="PRIVATE">Private</option>
              </select>
              <p className="error-text">{errors.visibility}</p>
            </div>
          </div>

          <button type="submit" className="create-event-button">
            {id ? `Update Event` : `Create Event`}
          </button>
        </form>
      </div>
    </div>
  );
}

export default CreateEvent;
