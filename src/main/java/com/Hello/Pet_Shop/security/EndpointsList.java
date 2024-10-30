package com.Hello.Pet_Shop.security;

public class EndpointsList {
    public static final String front_end_host = "http://localhost:3000";

    public static final String[] PUBLIC_ENDPOINTS = {
            "/authen/login",
            "/authen/loggedIn-data"
    };

    public static final String[] USER_ENDPOINTS = {
            "/user/user",
            "/user/updateUser/**" //With handle
    };

    public static final String[] ADMIN_ENDPOINTS = {
            "/user/addUser/**",
            "/user/getUserById/**",
            "/user/getUserByFirstName/**",
            "/user/getUserByEmail/**",
            "/user/getAllUser",
            "/user/updateUser/**",
            "/user/deleteUser/**",
            "/user/deleteAllUser",
            "/user/admin"

    };
}
