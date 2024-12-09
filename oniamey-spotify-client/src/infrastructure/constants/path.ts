export const ROUTES_CONSTANTS = {
    AUTHENTICATION: {
        path: "/",
        name: "spotify",
        children: {
            LOGIN: {
                path: "login",
                name: "login",
            },
            REGISTER: {
                path: "register",
                name: "register",
            },
            FORGOT_PASSWORD: {
                path: "forgot-password",
                name: "forgot-password",
            },
        }
    },
    ADMIN: {
        path: "/admin",
        name: "admin",
        children: {
            USER: {
                path: "user-management",
                name: "user-management",
            },
            SONG: {
                path: "song-management",
                name: "song-management",
            },
            LISTENING_TO_MUSIC: {
                path: "admin-listening-to-music",
                name: "admin-listening-to-music",
            },
        },
    },
    USER: {
        path: "/user",
        name: "user",
        children: {
            SONG: {
                path: "song",
                name: "song",
            },
            PLAYLIST: {
                path: "playlist",
                name: "playlist",
            },
            LISTENING_TO_MUSIC: {
                path: "user-listening-to-song",
                name: "user-listening-to-song",
            },
        },
    },
    NOT_FOUND: {
        path: "/:pathMatch(.*)*",
        name: "NotFound",
    },
    REDIRECT: {
        path: "/redirect",
        name: "redirect",
    },
};
