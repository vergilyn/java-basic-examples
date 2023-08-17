package com.vergilyn.examples.serialize;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UserInfo implements Serializable {
    private Long id;
    private String username;
    private String nickname;
    private GenderEnum gender;
    private String email;
    private String mobile;
    private String telephone;
    private LocalDate birthday;
    private Map<String, String> extra;
    private WebhookConfig webhookConfig;
    private List<Address> addresses;
    private Date updateTime = new Date();

    public static UserInfo newInstance(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(409839163L);
        userInfo.setUsername("vergilyn");
        userInfo.setNickname("用户昵称" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        userInfo.setGender(GenderEnum.PRIVACY);
        userInfo.setEmail("vergilyn@vip.qq.com");
        userInfo.setMobile("138xxxx0001");
        userInfo.setTelephone("023+47123456");
        userInfo.setBirthday(LocalDate.of(2000, 1, 1));

        Map<String, String> extra = new HashMap<>();
        extra.put("exrta-01", "value-01");
        extra.put("exrta-02", "value-02");
        userInfo.setExtra(extra);

        WebhookConfig config = new WebhookConfig();
        config.setAccessToken("bf88f6b4e53b99f1878f3cdb9d7a77569a4844cf43cb992ef899258f6fa6f409");
        config.setSecret("SECaf6dcb0be551d7dda0210b16b554f13b72b6795164c9ad5c4f1d1a675683b409");
        config.setName("webhook地址");
        userInfo.setWebhookConfig(config);

        userInfo.setAddresses(Lists.newArrayList(
                new Address("重庆", "000000"),
                new Address("四川", "000001")
        ));

        return userInfo;
    }

    @Data
    public static class WebhookConfig implements Serializable{
        private String accessToken;
        private String secret;
        private String name;
    }

    public static enum GenderEnum implements Serializable {
        MAN("男"), WOMAN("女"), PRIVACY("保密");

        public final String desc;

        GenderEnum(String desc) {
            this.desc = desc;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Address implements Serializable{
        private String city;
        private String postcode;

        public Address(String city, String postcode) {
            this.city = city;
            this.postcode = postcode;
        }
    }
}
