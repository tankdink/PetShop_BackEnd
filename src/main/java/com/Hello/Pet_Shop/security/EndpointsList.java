package com.Hello.Pet_Shop.security;

public class EndpointsList {
    public static final String front_end_host = "http://localhost:3000";

    public static final String[] PUBLIC_ENDPOINTS = {
            "/authen/login",
            "/authen/loggedIn-data"

    }; //FOR EVERYONE



    public static final String[] UPDATE_INFO_ENDPOINTS = {
            "/user/updateUser/**", //With handle
            "/user/updateUserPassword/**" //With handle

    }; //For USER, ACCOUNT_MANAGER, ADMIN

    public static final String[] MANAGE_ENDPOINTS = {
            "/user/addUser/**",
            "/user/getUserById/**",
            "/user/getUserByFirstName/**",
            "/user/getUserByEmail/**",
            "/user/getAllUser"

    }; // FOR ACCOUNT_MANAGER, ADMIN


    public static final String[] ADMIN_ENDPOINTS = {
            "/user/deleteUser/**",
            "/user/deleteAllUser"
    }; // FOR ADMIN
}
