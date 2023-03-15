package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import io.dcloud.common.adapter.util.Logger;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaCodec implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {
    private static final DateTimeFormatter ISO_FIXED_FORMAT = DateTimeFormat.forPattern(defaultPatttern).withZone(DateTimeZone.getDefault());
    private static final DateTimeFormatter defaultFormatter = DateTimeFormat.forPattern(defaultPatttern);
    private static final DateTimeFormatter defaultFormatter_23 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String defaultPatttern = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter_d10_cn = DateTimeFormat.forPattern("yyyy年M月d日");
    private static final DateTimeFormatter formatter_d10_de = DateTimeFormat.forPattern("dd.MM.yyyy");
    private static final DateTimeFormatter formatter_d10_eur = DateTimeFormat.forPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatter_d10_in = DateTimeFormat.forPattern("dd-MM-yyyy");
    private static final DateTimeFormatter formatter_d10_kr = DateTimeFormat.forPattern("yyyy년M월d일");
    private static final DateTimeFormatter formatter_d10_tw = DateTimeFormat.forPattern("yyyy/MM/dd");
    private static final DateTimeFormatter formatter_d10_us = DateTimeFormat.forPattern("MM/dd/yyyy");
    private static final DateTimeFormatter formatter_d8 = DateTimeFormat.forPattern(Logger.TIMESTAMP_YYYY_MM_DD);
    private static final DateTimeFormatter formatter_dt19_cn = DateTimeFormat.forPattern("yyyy年M月d日 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_cn_1 = DateTimeFormat.forPattern("yyyy年M月d日 H时m分s秒");
    private static final DateTimeFormatter formatter_dt19_de = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_eur = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_in = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_kr = DateTimeFormat.forPattern("yyyy년M월d일 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_tw = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_us = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_iso8601 = DateTimeFormat.forPattern(formatter_iso8601_pattern);
    private static final String formatter_iso8601_pattern = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String formatter_iso8601_pattern_23 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String formatter_iso8601_pattern_29 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    public static final JodaCodec instance = new JodaCodec();

    public int getFastMatchToken() {
        return 4;
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return deserialze(defaultJSONParser, type, obj, (String) null, 0);
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, String str, int i) {
        DateTimeFormatter dateTimeFormatter;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
            return null;
        }
        if (jSONLexer.token() == 4) {
            String stringVal = jSONLexer.stringVal();
            jSONLexer.nextToken();
            if (str != null) {
                dateTimeFormatter = defaultPatttern.equals(str) ? defaultFormatter : DateTimeFormat.forPattern(str);
            } else {
                dateTimeFormatter = null;
            }
            if ("".equals(stringVal)) {
                return null;
            }
            if (type == LocalDateTime.class) {
                if (stringVal.length() == 10 || stringVal.length() == 8) {
                    return parseLocalDate(stringVal, str, dateTimeFormatter).toLocalDateTime(LocalTime.MIDNIGHT);
                }
                return parseDateTime(stringVal, dateTimeFormatter);
            } else if (type == LocalDate.class) {
                if (stringVal.length() == 23) {
                    return LocalDateTime.parse(stringVal).toLocalDate();
                }
                return parseLocalDate(stringVal, str, dateTimeFormatter);
            } else if (type == LocalTime.class) {
                if (stringVal.length() == 23) {
                    return LocalDateTime.parse(stringVal).toLocalTime();
                }
                return LocalTime.parse(stringVal);
            } else if (type == DateTime.class) {
                if (dateTimeFormatter == defaultFormatter) {
                    dateTimeFormatter = ISO_FIXED_FORMAT;
                }
                return parseZonedDateTime(stringVal, dateTimeFormatter);
            } else if (type == DateTimeZone.class) {
                return DateTimeZone.forID(stringVal);
            } else {
                if (type == Period.class) {
                    return Period.parse(stringVal);
                }
                if (type == Duration.class) {
                    return Duration.parse(stringVal);
                }
                if (type == Instant.class) {
                    boolean z = false;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= stringVal.length()) {
                            z = true;
                            break;
                        }
                        char charAt = stringVal.charAt(i2);
                        if (charAt < '0' || charAt > '9') {
                            break;
                        }
                        i2++;
                    }
                    if (!z || stringVal.length() <= 8 || stringVal.length() >= 19) {
                        return Instant.parse(stringVal);
                    }
                    return new Instant(Long.parseLong(stringVal));
                } else if (type == DateTimeFormatter.class) {
                    return DateTimeFormat.forPattern(stringVal);
                }
            }
        } else if (jSONLexer.token() == 2) {
            long longValue = jSONLexer.longValue();
            jSONLexer.nextToken();
            TimeZone timeZone = JSON.defaultTimeZone;
            if (timeZone == null) {
                timeZone = TimeZone.getDefault();
            }
            if (type == DateTime.class) {
                return new DateTime(longValue, DateTimeZone.forTimeZone(timeZone));
            }
            T localDateTime = new LocalDateTime(longValue, DateTimeZone.forTimeZone(timeZone));
            if (type == LocalDateTime.class) {
                return localDateTime;
            }
            if (type == LocalDate.class) {
                return localDateTime.toLocalDate();
            }
            if (type == LocalTime.class) {
                return localDateTime.toLocalTime();
            }
            if (type == Instant.class) {
                return new Instant(longValue);
            }
            throw new UnsupportedOperationException();
        } else if (jSONLexer.token() == 12) {
            JSONObject parseObject = defaultJSONParser.parseObject();
            if (type == Instant.class) {
                Object obj2 = parseObject.get("epochSecond");
                if (obj2 instanceof Number) {
                    return Instant.ofEpochSecond(TypeUtils.longExtractValue((Number) obj2));
                }
                Object obj3 = parseObject.get("millis");
                if (obj3 instanceof Number) {
                    return Instant.ofEpochMilli(TypeUtils.longExtractValue((Number) obj3));
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00f4  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x012b A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.joda.time.LocalDateTime parseDateTime(java.lang.String r17, org.joda.time.format.DateTimeFormatter r18) {
        /*
            r16 = this;
            r0 = r17
            if (r18 != 0) goto L_0x014c
            int r1 = r17.length()
            r2 = 32
            r3 = 16
            r4 = 13
            r5 = 7
            r6 = 46
            r7 = 0
            r8 = 4
            r9 = 19
            r10 = 1
            r11 = 58
            r12 = 10
            r13 = 48
            r14 = 45
            if (r1 != r9) goto L_0x00bb
            char r1 = r0.charAt(r8)
            char r5 = r0.charAt(r5)
            char r15 = r0.charAt(r12)
            char r4 = r0.charAt(r4)
            char r3 = r0.charAt(r3)
            if (r4 != r11) goto L_0x00ea
            if (r3 != r11) goto L_0x00ea
            if (r1 != r14) goto L_0x004a
            if (r5 != r14) goto L_0x004a
            r1 = 84
            if (r15 != r1) goto L_0x0044
            org.joda.time.format.DateTimeFormatter r1 = formatter_iso8601
            goto L_0x00ec
        L_0x0044:
            if (r15 != r2) goto L_0x00ea
            org.joda.time.format.DateTimeFormatter r1 = defaultFormatter
            goto L_0x00ec
        L_0x004a:
            r2 = 47
            if (r1 != r2) goto L_0x0054
            if (r5 != r2) goto L_0x0054
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_tw
            goto L_0x00ec
        L_0x0054:
            char r3 = r0.charAt(r7)
            char r4 = r0.charAt(r10)
            r5 = 2
            char r5 = r0.charAt(r5)
            r11 = 3
            char r11 = r0.charAt(r11)
            r15 = 5
            char r15 = r0.charAt(r15)
            if (r5 != r2) goto L_0x00ad
            if (r15 != r2) goto L_0x00ad
            int r3 = r3 - r13
            int r3 = r3 * 10
            int r4 = r4 - r13
            int r3 = r3 + r4
            int r11 = r11 - r13
            int r11 = r11 * 10
            int r1 = r1 - r13
            int r11 = r11 + r1
            r1 = 12
            if (r3 <= r1) goto L_0x0081
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_eur
            goto L_0x00ec
        L_0x0081:
            if (r11 <= r1) goto L_0x0087
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_us
            goto L_0x00ec
        L_0x0087:
            java.util.Locale r1 = java.util.Locale.getDefault()
            java.lang.String r1 = r1.getCountry()
            java.lang.String r2 = "US"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x009a
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_us
            goto L_0x00ec
        L_0x009a:
            java.lang.String r2 = "BR"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00aa
            java.lang.String r2 = "AU"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x00ea
        L_0x00aa:
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_eur
            goto L_0x00ec
        L_0x00ad:
            if (r5 != r6) goto L_0x00b4
            if (r15 != r6) goto L_0x00b4
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_de
            goto L_0x00ec
        L_0x00b4:
            if (r5 != r14) goto L_0x00ea
            if (r15 != r14) goto L_0x00ea
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_in
            goto L_0x00ec
        L_0x00bb:
            int r1 = r17.length()
            r15 = 23
            if (r1 != r15) goto L_0x00ea
            char r1 = r0.charAt(r8)
            char r5 = r0.charAt(r5)
            char r12 = r0.charAt(r12)
            char r4 = r0.charAt(r4)
            char r3 = r0.charAt(r3)
            char r15 = r0.charAt(r9)
            if (r4 != r11) goto L_0x00ea
            if (r3 != r11) goto L_0x00ea
            if (r1 != r14) goto L_0x00ea
            if (r5 != r14) goto L_0x00ea
            if (r12 != r2) goto L_0x00ea
            if (r15 != r6) goto L_0x00ea
            org.joda.time.format.DateTimeFormatter r1 = defaultFormatter_23
            goto L_0x00ec
        L_0x00ea:
            r1 = r18
        L_0x00ec:
            int r2 = r17.length()
            r3 = 17
            if (r2 < r3) goto L_0x0116
            char r2 = r0.charAt(r8)
            r3 = 24180(0x5e74, float:3.3883E-41)
            if (r2 != r3) goto L_0x010f
            int r1 = r17.length()
            int r1 = r1 - r10
            char r1 = r0.charAt(r1)
            r2 = 31186(0x79d2, float:4.3701E-41)
            if (r1 != r2) goto L_0x010c
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_cn_1
            goto L_0x0116
        L_0x010c:
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_cn
            goto L_0x0116
        L_0x010f:
            r3 = 45380(0xb144, float:6.3591E-41)
            if (r2 != r3) goto L_0x0116
            org.joda.time.format.DateTimeFormatter r1 = formatter_dt19_kr
        L_0x0116:
            r2 = 0
        L_0x0117:
            int r3 = r17.length()
            if (r2 >= r3) goto L_0x012b
            char r3 = r0.charAt(r2)
            if (r3 < r13) goto L_0x012c
            r4 = 57
            if (r3 <= r4) goto L_0x0128
            goto L_0x012c
        L_0x0128:
            int r2 = r2 + 1
            goto L_0x0117
        L_0x012b:
            r7 = 1
        L_0x012c:
            if (r7 == 0) goto L_0x014e
            int r2 = r17.length()
            r3 = 8
            if (r2 <= r3) goto L_0x014e
            int r2 = r17.length()
            if (r2 >= r9) goto L_0x014e
            long r0 = java.lang.Long.parseLong(r17)
            org.joda.time.LocalDateTime r2 = new org.joda.time.LocalDateTime
            java.util.TimeZone r3 = com.alibaba.fastjson.JSON.defaultTimeZone
            org.joda.time.DateTimeZone r3 = org.joda.time.DateTimeZone.forTimeZone(r3)
            r2.<init>(r0, r3)
            return r2
        L_0x014c:
            r1 = r18
        L_0x014e:
            if (r1 != 0) goto L_0x0155
            org.joda.time.LocalDateTime r0 = org.joda.time.LocalDateTime.parse(r17)
            goto L_0x0159
        L_0x0155:
            org.joda.time.LocalDateTime r0 = org.joda.time.LocalDateTime.parse(r0, r1)
        L_0x0159:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JodaCodec.parseDateTime(java.lang.String, org.joda.time.format.DateTimeFormatter):org.joda.time.LocalDateTime");
    }

    /* access modifiers changed from: protected */
    public LocalDate parseLocalDate(String str, String str2, DateTimeFormatter dateTimeFormatter) {
        DateTimeFormatter dateTimeFormatter2;
        DateTimeFormatter dateTimeFormatter3;
        if (dateTimeFormatter == null) {
            if (str.length() == 8) {
                dateTimeFormatter = formatter_d8;
            }
            boolean z = false;
            if (str.length() == 10) {
                char charAt = str.charAt(4);
                char charAt2 = str.charAt(7);
                if (charAt == '/' && charAt2 == '/') {
                    dateTimeFormatter = formatter_d10_tw;
                }
                char charAt3 = str.charAt(0);
                char charAt4 = str.charAt(1);
                char charAt5 = str.charAt(2);
                char charAt6 = str.charAt(3);
                char charAt7 = str.charAt(5);
                if (charAt5 == '/' && charAt7 == '/') {
                    int i = ((charAt6 - '0') * 10) + (charAt - '0');
                    if (((charAt3 - '0') * 10) + (charAt4 - '0') > 12) {
                        dateTimeFormatter3 = formatter_d10_eur;
                    } else if (i > 12) {
                        dateTimeFormatter3 = formatter_d10_us;
                    } else {
                        String country = Locale.getDefault().getCountry();
                        if (country.equals("US")) {
                            dateTimeFormatter3 = formatter_d10_us;
                        } else if (country.equals("BR") || country.equals("AU")) {
                            dateTimeFormatter3 = formatter_d10_eur;
                        }
                    }
                    dateTimeFormatter = dateTimeFormatter3;
                } else if (charAt5 == '.' && charAt7 == '.') {
                    dateTimeFormatter = formatter_d10_de;
                } else if (charAt5 == '-' && charAt7 == '-') {
                    dateTimeFormatter = formatter_d10_in;
                }
            }
            if (str.length() >= 9) {
                char charAt8 = str.charAt(4);
                if (charAt8 == 24180) {
                    dateTimeFormatter2 = formatter_d10_cn;
                } else if (charAt8 == 45380) {
                    dateTimeFormatter2 = formatter_d10_kr;
                }
                dateTimeFormatter = dateTimeFormatter2;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= str.length()) {
                    z = true;
                    break;
                }
                char charAt9 = str.charAt(i2);
                if (charAt9 < '0' || charAt9 > '9') {
                    break;
                }
                i2++;
            }
            if (z && str.length() > 8 && str.length() < 19) {
                return new LocalDateTime(Long.parseLong(str), DateTimeZone.forTimeZone(JSON.defaultTimeZone)).toLocalDate();
            }
        }
        if (dateTimeFormatter == null) {
            return LocalDate.parse(str);
        }
        return LocalDate.parse(str, dateTimeFormatter);
    }

    /* access modifiers changed from: protected */
    public DateTime parseZonedDateTime(String str, DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter == null) {
            if (str.length() == 19) {
                char charAt = str.charAt(4);
                char charAt2 = str.charAt(7);
                char charAt3 = str.charAt(10);
                char charAt4 = str.charAt(13);
                char charAt5 = str.charAt(16);
                if (charAt4 == ':' && charAt5 == ':') {
                    if (charAt == '-' && charAt2 == '-') {
                        if (charAt3 == 'T') {
                            dateTimeFormatter = formatter_iso8601;
                        } else if (charAt3 == ' ') {
                            dateTimeFormatter = defaultFormatter;
                        }
                    } else if (charAt == '/' && charAt2 == '/') {
                        dateTimeFormatter = formatter_dt19_tw;
                    } else {
                        char charAt6 = str.charAt(0);
                        char charAt7 = str.charAt(1);
                        char charAt8 = str.charAt(2);
                        char charAt9 = str.charAt(3);
                        char charAt10 = str.charAt(5);
                        if (charAt8 == '/' && charAt10 == '/') {
                            int i = ((charAt9 - '0') * 10) + (charAt - '0');
                            if (((charAt6 - '0') * 10) + (charAt7 - '0') > 12) {
                                dateTimeFormatter = formatter_dt19_eur;
                            } else if (i > 12) {
                                dateTimeFormatter = formatter_dt19_us;
                            } else {
                                String country = Locale.getDefault().getCountry();
                                if (country.equals("US")) {
                                    dateTimeFormatter = formatter_dt19_us;
                                } else if (country.equals("BR") || country.equals("AU")) {
                                    dateTimeFormatter = formatter_dt19_eur;
                                }
                            }
                        } else if (charAt8 == '.' && charAt10 == '.') {
                            dateTimeFormatter = formatter_dt19_de;
                        } else if (charAt8 == '-' && charAt10 == '-') {
                            dateTimeFormatter = formatter_dt19_in;
                        }
                    }
                }
            }
            if (str.length() >= 17) {
                char charAt11 = str.charAt(4);
                if (charAt11 == 24180) {
                    if (str.charAt(str.length() - 1) == 31186) {
                        dateTimeFormatter = formatter_dt19_cn_1;
                    } else {
                        dateTimeFormatter = formatter_dt19_cn;
                    }
                } else if (charAt11 == 45380) {
                    dateTimeFormatter = formatter_dt19_kr;
                }
            }
        }
        if (dateTimeFormatter == null) {
            return DateTime.parse(str);
        }
        return DateTime.parse(str, dateTimeFormatter);
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        if (type == null) {
            type = obj.getClass();
        }
        if (type == LocalDateTime.class) {
            int mask = SerializerFeature.UseISO8601DateFormat.getMask();
            LocalDateTime localDateTime = (LocalDateTime) obj;
            String dateFormatPattern = jSONSerializer.getDateFormatPattern();
            if (dateFormatPattern == null) {
                if ((mask & i) != 0 || jSONSerializer.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
                    dateFormatPattern = formatter_iso8601_pattern;
                } else {
                    dateFormatPattern = jSONSerializer.isEnabled(SerializerFeature.WriteDateUseDateFormat) ? JSON.DEFFAULT_DATE_FORMAT : localDateTime.getMillisOfSecond() == 0 ? formatter_iso8601_pattern_23 : formatter_iso8601_pattern_29;
                }
            }
            if (dateFormatPattern != null) {
                write(serializeWriter, (ReadablePartial) localDateTime, dateFormatPattern);
            } else {
                serializeWriter.writeLong(localDateTime.toDateTime(DateTimeZone.forTimeZone(JSON.defaultTimeZone)).toInstant().getMillis());
            }
        } else {
            serializeWriter.writeString(obj.toString());
        }
    }

    public void write(JSONSerializer jSONSerializer, Object obj, BeanContext beanContext) throws IOException {
        write(jSONSerializer.out, (ReadablePartial) obj, beanContext.getFormat());
    }

    private void write(SerializeWriter serializeWriter, ReadablePartial readablePartial, String str) {
        DateTimeFormatter dateTimeFormatter;
        if (str.equals(formatter_iso8601_pattern)) {
            dateTimeFormatter = formatter_iso8601;
        } else {
            dateTimeFormatter = DateTimeFormat.forPattern(str);
        }
        serializeWriter.writeString(dateTimeFormatter.print(readablePartial));
    }
}
