package com.droi.sdk.featuretestapp;

import android.os.Environment;
import android.util.Log;

import com.droi.sdk.DroiError;
import com.droi.sdk.core.DroiCondition;
import com.droi.sdk.core.DroiFile;
import com.droi.sdk.core.DroiGroup;
import com.droi.sdk.core.DroiObject;
import com.droi.sdk.core.DroiPermission;
import com.droi.sdk.core.DroiQuery;
import com.droi.sdk.core.DroiUser;
import com.droi.sdk.core.generated.QCustomBook;
import com.droi.sdk.core.generated.QTestData;


//
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DroiBaaSTestCases {

    public static boolean init() {
        return true;
    }

    //
    public static void checkResult(boolean result, String text) {
        if (result) {
            Log.i(LOG_TAG, text + " is ok.");
        } else {
            Log.i(LOG_TAG, text + " is failed.");
        }
    }

    public static void checkStringResult(String result, String text) {
        if (result == null || result.isEmpty()) {
            Log.i(LOG_TAG, text + " is ok.");
        } else {
            Log.i(LOG_TAG, text + " is " + result);
        }
    }

    // Data - CRUD
    public static Result dataInit() {
//        DroiQuery query = DroiQuery.Builder.newBuilder().delete( TestData.class ).build();
//        DroiError err = new DroiError( DroiError.OK, null);
//        query.runQuery( err );
//        if ( err.isOk() == false ) {
//            Log.e( LOG_TAG, "The error is " + err.toString() );
//        }
//        return err.isOk();
        DroiQuery query = DroiQuery.Builder.newBuilder().query(TestData.class).limit(500).build();
        DroiError err = new DroiError(DroiError.OK, null);
        List<TestData> res = query.runQuery(err);
        if (!err.isOk()) {
            Log.e(LOG_TAG, "The error is " + err.toString());
            return new Result(false, err);
        }

        if (res == null) {
            return new Result(false, new DroiError(DroiError.ERROR, "res == null"));
        }

        if (res.size() > 0)
            err = DroiObject.deleteAll(res);
        if (!err.isOk()) {
            Log.e(LOG_TAG, "The error is " + err.toString());
            return new Result(false, err);
        }

        return new Result(true, err);
    }

    public static Result dataCreate() {
        Result result = new Result(true, new DroiError(DroiError.OK, null));
        for (int i = 0; i < texts.length; i++) {
            TestData data = new TestData();
            data.value = values[i];
            data.name = texts[i];
            data.description = String.format("%s-%d", texts[i], values[i]);
            DroiError err = data.save();
            if (!err.isOk()) {
                Log.e(LOG_TAG, "The error is " + err.toString());
                result = new Result(false, err);
                return result;
            }
        }
        return result;
    }

    public static StringResult dataRead() {
        // 1. Select ALL
        DroiQuery query = DroiQuery.Builder.newBuilder().query(TestData.class).build();
        DroiError err = new DroiError(DroiError.OK, null);
        String result = "";
        List<TestData> res = query.runQuery(err);
        if (!err.isOk() || res.size() != texts.length) {
            result = "Query data is failed";
            Log.e(LOG_TAG, "Return size is " + res.size() + "The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 2. Select ALL with order by value
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .orderBy(QTestData.value, false)
                .build();
        err = new DroiError(DroiError.OK, null);
        result = "";
        res = query.runQuery(err);
        if (!err.isOk() || res.size() != texts.length) {
            result = "Query data is failed";
            Log.e(LOG_TAG, "Return size is " + res.size() + "The error is " + err.toString());
            return new StringResult(result, err);
        }

        int value = 100000;
        for (TestData td : res) {
            if (value < td.value) {
                result = "Query data with orderby(false) is failed";
                Log.e(LOG_TAG, result);
                return new StringResult(result, err);
            }
            value = td.value;
        }

        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .orderBy(QTestData.value, true)
                .build();
        err = new DroiError(DroiError.OK, null);
        result = "";
        res = query.runQuery(err);
        if (!err.isOk() || res.size() != texts.length) {
            result = "Query data is failed";
            Log.e(LOG_TAG, "Return size is " + res.size() + "The error is " + err.toString());
            return new StringResult(result, err);
        }

        value = 0;
        for (TestData td : res) {
            if (value > td.value) {
                result = "Query data with orderby(true) is failed";
                Log.e(LOG_TAG, result);
                return new StringResult(result, err);
            }
            value = td.value;
        }


        // 3. eq
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_eq(111))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 1 || !res.get(0).name.equals("com")) {
            result = "Query with eq is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 4. neq
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_neq(111))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 4) {
            result = "Query with neq is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        for (TestData data : res) {
            if (data.value == 111) {
                result = "neq value error";
                return new StringResult(result, err);
            }
        }

        // 5. ltOrEq
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_ltOrEq(111))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 1 || !res.get(0).name.equals("com")) {
            result = "Query with ltOrEq is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 6. gt
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_gt(111))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 4) {
            result = "Query with gt is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        for (TestData data : res) {
            if (data.value == 111) {
                result = "gt value error";
                return new StringResult(result, err);
            }
        }

        // 7. gtOrEq
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_gtOrEq(555))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 1 || !res.get(0).name.equals("droiQuery")) {
            result = "Query with gtOrEq is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }


        // 8. contain
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_contains("droi"))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 2) {
            result = "Query with contains is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 9. notcontains
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_notcontains("droi"))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 3) {
            result = "Query with notcontains is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 10. startWith
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_startsWith("droi"))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 2) {
            result = "Query with contains is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 11. notStartWith
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_notStartsWith("droi"))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 3) {
            result = "Query with notStartsWith( is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 12. endWith
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_endsWith("app"))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 2) {
            result = "Query with endsWith is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 13. notEndsWith
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_notEndsWith("app"))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 3) {
            result = "Query with notEndsWith is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 14. in
        ArrayList<String> condition = new ArrayList<String>();
        condition.add("com");
        condition.add("sdk");

        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_in(condition))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 2) {
            result = "Query with In is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 15. in
        ArrayList<Integer> cond_int = new ArrayList<Integer>();
        cond_int.add(444);
        cond_int.add(555);

        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_in(cond_int))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 2) {
            result = "Query with In(Integer) is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 16. non_in
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.name_notIn(condition))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 3) {
            result = "Query with notIn is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 17. value <= 333 && startsWith = 'sdk'
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_ltOrEq(333).and(QTestData.name_startsWith("sdk")))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 1 || res.get(0).value != 333 || !res.get(0).name.equals("sdk")) {
            result = "Query with (value <= 333 && startsWith = 'sdk') is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 18. value == 333 || startsWith = 'com'
        query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_eq(333).or(QTestData.name_startsWith("com")))
                .build();
        res = query.runQuery(err);

        if (!err.isOk() || res.size() != 2) {
            result = "Query with (value == 333 || startsWith = 'com') is failed";
            Log.e(LOG_TAG, "Query with condition is failed. The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 19. Remove all data in KeyValue table
        query = DroiQuery.Builder.newBuilder().query("KeyValue").build();
        List<DroiObject> res2 = query.runQuery(err);
        if (!err.isOk()) {
            result = "Query KeyValue table is failed";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }

        if (res2 != null) {
            for (DroiObject obj : res2) {
                err = obj.delete();
                if (!err.isOk()) {
                    result = "Delete DroiObject from KeyValue is failed";
                    Log.e(LOG_TAG, result + " The error is " + err.toString());
                    return new StringResult(result, err);
                }
            }
        }

        // 20. Add data into KeyValue table
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            DroiObject obj = DroiObject.create("KeyValue");
            obj.put("num", rand.nextInt(10000));
            obj.put("val", String.format("The value is %d", i + 2));
            err = obj.save();
            if (!err.isOk()) {
                result = "Add DroiObject into KeyValue is failed";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        // 21. Query data from KeyValue with orderBy num
        query = DroiQuery.Builder.newBuilder()
                .query("KeyValue")
                .orderBy("num", true)
                .build();
        res2 = query.runQuery(err);
        if (!err.isOk() && res2.size() == 10) {
            result = "Query KeyValue table is failed";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }

        int m = -2;
        for (int i = 0; i < 10; i++) {
            DroiObject obj = res2.get(i);
            if (m > obj.getInt("num")) {
                result = "Query KeyValue table with order by is failed";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }

            m = obj.getInt("num");
        }
        return new StringResult(result, err);
    }

    public static StringResult dataUpdate() {
        DroiQuery query = DroiQuery.Builder.newBuilder().query(TestData.class)
                .where(QTestData.value_eq(333))
                .build();
        DroiError err = new DroiError(DroiError.OK, null);
        List<TestData> res = query.runQuery(err);
        if (!err.isOk() || res == null || res.size() != 1 || !res.get(0).name.equals("sdk")) {
            String result = "Cannot result correct data";
            return new StringResult(result, err);
        }

        TestData td = res.get(0);
        td.name = "sdksdk";
        err = td.save();
        if (!err.isOk()) {
            String result = "Update Data failed. The error is ";
            return new StringResult(result, err);
        }

        res = query.runQuery(err);
        if (!err.isOk() || res == null || res.size() != 1 || !res.get(0).name.equals("sdksdk")) {
            String result = "Cannot result correct data";
            return new StringResult(result, err);
        }

        return new StringResult("", err);
    }

    public static Result dataDelete() {
        DroiQuery query = DroiQuery.Builder.newBuilder()
                .where(QTestData.value_gtOrEq(333))
                .query(TestData.class)
                .build();
        DroiError err = new DroiError(DroiError.OK, null);
        List<TestData> res = query.runQuery(err);
        if (!err.isOk())
            return new Result(false, err);

        for (int i = 0; i < res.size(); i++)
            res.get(i).delete();

        query = DroiQuery.Builder.newBuilder()
                .query(TestData.class)
                .where(QTestData.value_ltOrEq(333))
                .build();
        res = query.runQuery(err);
        if (!err.isOk())
            return new Result(false, err);
        if (res == null || res.size() != 2) {
            return new Result(false, new DroiError(DroiError.ERROR, "res == null || res.size() != 2"));
        }

        return new Result(true, err);
    }

    public static Result aclTest() {
        // Remove all data
        dataInit();
        DroiError error;
        // Create two users
        DroiUser user = DroiUser.getCurrentUser();
        if (user.isAuthorized()) {
            error = user.logout();
            if (!error.isOk()) {
                return new Result(false, error);
            }

        }
        DroiUser user1 = DroiUser.login("dta_user1", "123456", null);
        if (user1 == null) {
            user1 = new DroiUser();
            user1.setUserId("dta_user1");
            user1.setPassword("123456");
            error = user1.signUp();
            if (!error.isOk()) {
                return new Result(false, error);
            }
        }

        DroiUser user2 = DroiUser.login("dta_user2", "123456", null);
        if (user2 == null) {
            user2 = new DroiUser();
            user2.setUserId("dta_user2");
            user2.setPassword("123456");
            error = user2.signUp();
            if (!error.isOk()) {
                return new Result(false, error);
            }
        }

        DroiQuery query = DroiQuery.Builder.newBuilder().query(DroiGroup.class).where("Name", DroiCondition.Type.EQ, "DTA_group").build();
        List<DroiGroup> list = query.runQuery(null);
        DroiGroup group;
        if (list.size() == 0) {
            DroiPermission permission = new DroiPermission();
            permission.setUserReadPermission(user1.getObjectId(), true);
            permission.setUserWritePermission(user1.getObjectId(), true);
            permission.setUserReadPermission(user2.getObjectId(), true);
            permission.setUserWritePermission(user2.getObjectId(), true);

            group = new DroiGroup();
            group.setName("DTA_group");
            group.setPermission(permission);
        } else {
            group = list.get(0);
            error = group.fetch();
            if (!error.isOk()) {
                return new Result(false, error);
            }
        }

        // Add user
        DroiPermission permission = group.getPermission();
        permission.setUserReadPermission(user1.getObjectId(), true);
        permission.setUserWritePermission(user1.getObjectId(), true);
        permission.setUserReadPermission(user2.getObjectId(), true);
        permission.setUserWritePermission(user2.getObjectId(), true);
        group.setPermission(permission);
        group.addUser(user1.getObjectId());
        group.addUser(user2.getObjectId());
        DroiError err = group.save();
        if (!err.isOk()) {
            return new Result(false, err);
        }

        // Add new data with group permission
        for (int i = 0; i < texts.length; i++) {
            TestData data = new TestData();
            data.value = values[i];
            data.name = texts[i];
            data.description = String.format("%s-%d", texts[i], values[i]);
            DroiPermission perm = new DroiPermission();
            perm.setGroupReadPermission(group.getObjectId(), true);
            perm.setGroupWritePermission(group.getObjectId(), true);
            data.setPermission(perm);

            err = data.save();
            if (!err.isOk()) {
                Log.e(LOG_TAG, "The error is " + err.toString());
                if (!err.isOk()) {
                    return new Result(false, err);
                }
            }

            //
        }

        // Remove user1 from group
        group.removeUser(user1.getObjectId());
        err = group.save();
        if (!err.isOk()) {
            return new Result(false, err);
        }

        // User 2 -> logout
        // User 1 -> login
        user2.logout();
        user1 = DroiUser.login("dta_user1", "123456", err);
        if (!err.isOk()) {
            return new Result(false, err);
        }

        //
        query = DroiQuery.Builder.newBuilder().query(TestData.class).build();
        List<TestData> res = query.runQuery(err);
        if (!err.isOk()) {
            Log.e(LOG_TAG, "run data from cloud is failed");
            return new Result(false, err);
        }

        if (res.size() > 0) {
            return new Result(false, new DroiError(DroiError.ERROR, "res.size() > 0"));
        }

        group.addUser(user1.getObjectId());
        err = group.save();
        if (!err.isOk())
            return new Result(false, err);

        res = query.runQuery(err);
        if (!err.isOk()) {
            Log.e(LOG_TAG, "run data from cloud is failed");
            return new Result(false, err);
        }

        if (res.size() == 0) {
            Log.e(LOG_TAG, "should one record");
            return new Result(false, new DroiError(DroiError.ERROR, "should one record"));
        }

        for (TestData data : res) {
            data.delete();
        }

        user1.logout();

        return new Result(true, null);
    }

    public static String LOG_TAG = "DroiBaaSTextCases";
    static String[] texts = new String[]{"com", "droiapp", "sdk", "featuretestapp", "droiQuery"};
    static int[] values = new int[]{111, 222, 333, 444, 555};


    public static StringResult ClearAllData() {
        // 22. Query all data from Author/Book table
        DroiError err = new DroiError();
        String result;
        DroiQuery query = DroiQuery.Builder.newBuilder()
                .query(CustomBook.class)
                .build();
        List<CustomBook> res4 = query.runQuery(err);
        if (!err.isOk()) {
            result = "Query all data from Book is failed.";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }
        for (int i = 0; i < res4.size(); i++) {
            err = res4.get(i).delete();
            if (!err.isOk()) {
                result = "Delete Book data is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        query = DroiQuery.Builder.newBuilder()
                .query(CustomAuthor.class)
                .build();
        List<CustomAuthor> res3 = query.runQuery(err);
        if (!err.isOk()) {
            result = "Query all data from Author is failed.";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }
        for (int i = 0; i < res3.size(); i++) {
            err = res3.get(i).delete();
            if (!err.isOk()) {
                result = "Delete Author data is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        query = DroiQuery.Builder.newBuilder()
                .query(TestData.class)
                .build();
        List<TestData> res = query.runQuery(err);
        if (!err.isOk()) {
            return new StringResult("Query TestData failed when delete", err);
        }

        for (int i = 0; i < res.size(); i++) {
            err = res.get(i).delete();
            if (!err.isOk()) {
                result = "Delete TestData is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        query = DroiQuery.Builder.newBuilder()
                .query("KeyValue")
                .build();
        List<DroiObject> res2 = query.runQuery(err);
        if (!err.isOk()) {
            return new StringResult("Query KeyValue failed when delete", err);
        }

        for (int i = 0; i < res2.size(); i++) {
            err = res2.get(i).delete();
            if (!err.isOk()) {
                result = "Delete KeyValue is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        query = DroiQuery.Builder.newBuilder()
                .query(MySongs.class)
                .build();
        List<MySongs> res5 = query.runQuery(err);
        if (!err.isOk()) {
            return new StringResult("Query MySongs failed when delete", err);
        }

        for (int i = 0; i < res5.size(); i++) {
            err = res5.get(i).delete();
            if (!err.isOk()) {
                result = "Delete MySongs is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        return new StringResult("", err);
    }

    public static StringResult DroiReferenceTest() {
        // 22. Query all data from Author/Book table
        DroiError err = new DroiError();
        String result;
        DroiQuery query = DroiQuery.Builder.newBuilder()
                .query(CustomBook.class)
                .build();
        List<CustomBook> res4 = query.runQuery(err);
        if (!err.isOk()) {
            result = "Query all data from Book is failed.";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }
        for (int i = 0; i < res4.size(); i++) {
            err = res4.get(i).delete();
            if (!err.isOk()) {
                result = "Delete Book data is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        query = DroiQuery.Builder.newBuilder()
                .query(CustomAuthor.class)
                .build();
        List<CustomAuthor> res3 = query.runQuery(err);
        if (!err.isOk()) {
            result = "Query all data from Author is failed.";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }
        for (int i = 0; i < res3.size(); i++) {
            err = res3.get(i).delete();
            if (!err.isOk()) {
                result = "Delete Author data is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }


        // 23. Insert reference data
        CustomAuthor ca = null;
        CustomBook cb;
        for (int i = 0; i < 10; i++) {
            ca = new CustomAuthor();
            cb = new CustomBook();
            cb.author = ca;
            ca.name = String.format("Name-%d", i);
            ca.Country = "TW";
            cb.name = String.format("BookName-%d", i);
            cb.price = i * 123 + 100;

            err = cb.save();
            if (!err.isOk()) {
                result = "Save Book data is failed.";
                Log.e(LOG_TAG, result + " The error is " + err.toString());
                return new StringResult(result, err);
            }
        }

        // 24. Update data
        ca.name = "101010";
        err = ca.save();
        if (!err.isOk()) {
            result = "Update author data is failed.";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }

        // 25. Query data
        query = DroiQuery.Builder.newBuilder()
                .query(CustomBook.class)
                .where(QCustomBook.name_eq("BookName-9"))
                .build();
        res4 = query.runQuery(err);
        if (!err.isOk() || res4.size() != 1 || !res4.get(0).author.name.equals("101010")) {
            result = "Query CustomBook data is failed.";
            Log.e(LOG_TAG, result + " The error is " + err.toString());
            return new StringResult(result, err);
        }
        return new StringResult("", err);
    }

    public static StringResult DroiFileUpload() {
        DroiFile cover = new DroiFile(new File(Environment.getExternalStorageDirectory() + File.separator + "1.png"), "png");
        MySongs song = new MySongs();
        song.Name = "My heart will go on";
        song.Cover = cover;
        DroiError err = song.save();
        if (!err.isOk()) {
            return new StringResult("Upload File Failed", err);
        }
        return new StringResult("", err);
    }

    public static StringResult DroiFileDownload() {
        DroiError err = new DroiError(DroiError.OK, "ok");
        DroiQuery query = DroiQuery.Builder.newBuilder().cloudStorage().query(MySongs.class).build();
        List<MySongs> list = query.runQuery(err); // DroiError 传 null 表示不需要知道错误
        if (!err.isOk() || list.size() != 1) {
            return new StringResult("Query File Failed", err);
        }
        MySongs song = list.get(0);
        byte[] coverData = song.Cover.get(err); // DroiError 传 null 表示不需要知道错误
        if (!err.isOk()) {
            return new StringResult("Download File Failed ", err);
        }
        if (coverData == null || coverData.length == 0) {
            return new StringResult("File Not ready", err);
        }
        return new StringResult("", err);
    }
}
