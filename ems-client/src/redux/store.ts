import { combineReducers, configureStore } from "@reduxjs/toolkit"
import UserReducer from "./slice/userSlice"
import {
    persistStore,
    persistReducer,
    FLUSH,
    REHYDRATE,
    PAUSE,
    PERSIST,
    PURGE,
    REGISTER,
} from "redux-persist";
import storage from "redux-persist/lib/storage";

const initialState = {
    version: 1.0,
}
const persistConfig = {
    key: 'root',
    version: initialState.version,
    storage
}

const rootReducer = combineReducers({
    user: UserReducer
})


const configurePersistedStore = () => {
    if (typeof window !== "undefined") {
        const storedVersion = localStorage.getItem("rootVersion");
        if (storedVersion && storedVersion !== String(initialState.version)) {
            localStorage.removeItem("persist:root");
            localStorage.setItem("rootVersion", String(initialState.version));
        }
    }

    const persistedReducer = persistReducer(persistConfig, rootReducer);
    const store = configureStore({
        reducer: persistedReducer,
        middleware: (getDefaultMiddleware) =>
            getDefaultMiddleware({
                serializableCheck: {
                    ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
                },
            }),
    })

    return {
        store,
        persistor: persistStore(store),
    };
}

const { store, persistor } = configurePersistedStore();

export { store, persistor };

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch