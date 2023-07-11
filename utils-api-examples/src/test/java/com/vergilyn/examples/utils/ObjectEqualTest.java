package com.vergilyn.examples.utils;


import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class ObjectEqualTest {

    /**
     * 期望：在不重写`equals`和`hashcode`的情况下，深度比较自定义对象是否相等（所有字段的值都相等）
     *
     * <br/> 依赖`org.apache.commons:commons-lang3:3.12.0`
     *
     * <p> FIXME 2023-05-31，存在bug，无法对 {@code ArrayList<Email>} 中的 `Email`进行比较，
     *      `EqualsBuilder`源码中是比较的 `ArrayList`,，而不是比较的 `Email`
     *
     * @see org.apache.commons.lang3.builder.EqualsBuilder#reflectionEquals(Object, Object, String...)
     */
    @Test
    void usingEqualsBuilder(){
        Person person1 = new Person();
        person1.setName("person1");
        person1.setBirthday(new Birthday(LocalDate.now()));
        person1.setEmails(Lists.newArrayList(
                new Email("123@qq.com", "123"),
                new Email("456@qq.com", "456")
        ));

        Person person2 = new Person();
        person2.setName("person2");
        person2.setBirthday(new Birthday(LocalDate.now()));
        person2.setEmails(Lists.newArrayList(
                new Email("1234@qq.com", "123a"),
                new Email("456@qq.com", "456a")
        ));

        // testTransients – whether to include transient fields
        // reflectUpToClass – the superclass to reflect up to (inclusive), may be null
        // testRecursive – whether to call reflection equals on non primitive fields recursively.
        // excludeFields – array of field names to exclude from testing

        // XXX 2023-05-31 `excludeFields` 例如示例代码中的`name`: `Person.name`、`Email.name`。
        //  实际是都包括，无法单独指定成 `Person.name`
        Assertions.assertThat(person1)
                .usingComparator((o1, o2) ->
                    EqualsBuilder.reflectionEquals(person1, person2, false, null, true, "name") ? 0 : 1)
                .isNotEqualTo(person2);

    }

    @Setter
    @Getter
    static class Person {
        private String name;
        private Birthday birthday;
        private List<Email> emails;
    }

    @Setter
    @Getter
    static class Birthday {
        private Integer year;
        private Integer month;
        private Integer day;
        private LocalDate date;

        public Birthday(LocalDate date) {
            this.date = date;
            this.year = date.getYear();
            this.month = date.getMonthValue();
            this.day = date.getDayOfMonth();
        }
    }

    @Setter
    @Getter
    static class Email {
        private String email;
        private String name;

        public Email(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }
}
