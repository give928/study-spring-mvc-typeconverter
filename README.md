# 김영한님 스프링 MVC 스터디

- [서블릿](https://github.com/give928/study-spring-servlet)
- [타임리프](https://github.com/give928/study-spring-mvc-thymeleaf)
- [아이템 서비스](https://github.com/give928/study-spring-mvc-item-service)
- [메시지, 국제화](https://github.com/give928/study-spring-mvc-message)
- [검증](https://github.com/give928/study-spring-mvc-validation)
- [로그인, 필터, 스프링 인터셉터](https://github.com/give928/study-spring-mvc-login)
- [예외 처리와 오류 페이지](https://github.com/give928/study-spring-mvc-exception)
- [스프링 타입 컨버터](https://github.com/give928/study-spring-mvc-typeconverter)
- [파일 업로드](https://github.com/give928/study-spring-mvc-upload)

## 스프링 타입 컨버터

**스프링의 타입 변환 적용 예**

- 스프링 MVC 요청 파라미터
  @RequestParam , @ModelAttribute , @PathVariable
- @Value 등으로 YML 정보 읽기
- XML에 넣은 스프링 빈 정보를 변환
- 뷰를 렌더링 할 때

스프링은 확장 가능한 컨버터 인터페이스를 제공한다.
개발자는 스프링에 추가적인 타입 변환이 필요하면 이 컨버터 인터페이스를 구현해서 등록하면 된다.
이 컨버터 인터페이스는 모든 타입에 적용할 수 있다.

타입 컨버터를 사용하려면 org.springframework.core.convert.converter.Converter 인터페이스를 구현하면 된다.

스프링은 용도에 따라 다양한 방식의 타입 컨버터를 제공한다.

- Converter 기본 타입 컨버터
- ConverterFactory 전체 클래스 계층 구조가 필요할 때
- GenericConverter 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
- ConditionalGenericConverter 특정 조건이 참인 경우에만 실행

스프링은 문자, 숫자, 불린, Enum등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공한다. IDE에서 Converter , ConverterFactory , GenericConverter 의 구현체를 찾아보면 수 많은 컨버터를 확인할 수 있다.

### **컨버전 서비스 - ConversionService**

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    }
}
```

**처리 과정**

@RequestParam 은 @RequestParam 을 처리하는 ArgumentResolver 인
RequestParamMethodArgumentResolver 에서 ConversionService 를 사용해서 타입을 변환한다. 부모
클래스와 다양한 외부 클래스를 호출하는 등 복잡한 내부 과정을 거치기 때문에 대략 이렇게 처리되는
것으로 이해해도 충분하다. 만약 더 깊이있게 확인하고 싶으면 IpPortConverter 에 디버그 브레이크
포인트를 걸어서 확인해보자.

### 뷰 템플릿에 컨버터 적용하기

**변수 표현식 :** ${...}
**컨버전 서비스 적용 :** ${{...}}

폼에 적용하기
타임리프의 th:field 는 앞서 설명했듯이 id , name 를 출력하는 등 다양한 기능이 있는데, 여기에 컨버전 서비스도 함께 적용된다.

### 포맷터 - Formatter

객체를 특정한 포멧에 맞추어 문자로 출력하거나 또는 그 반대의 역할을 하는 것에 특화된 기능이 바로 포맷터( Formatter )이다. 포맷터는 컨버터의 특별한 버전으로 이해하면 된다.

**Converter vs Formatter**

- Converter 는 범용(객체 객체)
- Formatter 는 문자에 특화(객체 문자, 문자 객체) + 현지화(Locale)
    - Converter 의 특별한 버전

NumberFormat.getInstance(locale).parse(text); // 문자를 숫자로

NumberFormat.getInstance(locale).format(number); // 숫자를 문자로

스프링은 용도에 따라 다양한 방식의 포맷터를 제공한다.
> Formatter 포맷터
> AnnotationFormatterFactory 필드의 타입이나 애노테이션 정보를 활용할 수 있는 포맷터
>
> 자세한 내용은 공식 문서를 참고하자.
> https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#format

### **포맷터를 지원하는 컨버전 서비스**

포맷터를 지원하는 컨버전 서비스를 사용하면 컨버전 서비스에 포맷터를 추가할 수 있다. 내부에서 어댑터 패턴을 사용해서 Formatter 가 Converter 처럼 동작하도록 지원한다.

FormattingConversionService 는 포맷터를 지원하는 컨버전 서비스이다. DefaultFormattingConversionService 는 FormattingConversionService 에 기본적인 통화, 숫자 관련 몇가지 기본 포맷터를 추가해서 제공한다.

### **스프링이 제공하는 기본 포맷터**

스프링은 이런 문제를 해결하기 위해 애노테이션 기반으로 원하는 형식을 지정해서 사용할 수 있는 매우
유용한 포맷터 두 가지를 기본으로 제공한다.

@NumberFormat : 숫자 관련 형식 지정 포맷터 사용, NumberFormatAnnotationFormatterFactory
@DateTimeFormat : 날짜 관련 형식 지정 포맷터 사용, Jsr310DateTimeFormatAnnotationFormatterFactory

주의!
메시지 컨버터( HttpMessageConverter )에는 컨버전 서비스가 적용되지 않는다.
특히 객체를 JSON으로 변환할 때 메시지 컨버터를 사용하면서 이 부분을 많이 오해하는데, HttpMessageConverter 의 역할은 HTTP 메시지 바디의 내용을 객체로 변환하거나 객체를 HTTP 메시지 바디에 입력하는 것이다. 예를 들어서 JSON을 객체로 변환하는 메시지 컨버터는 내부에서 Jackson 같은 라이브러리를 사용한다. 객체를 JSON으로 변환한다면 그 결과는 이 라이브러리에 달린 것이다. 따라서 JSON 결과로 만들어지는 숫자나 날짜 포맷을 변경하고 싶으면 해당 라이브러리가 제공하는 설정을 통해서 포맷을 지정해야 한다. 결과적으로 이것은 컨버전 서비스와 전혀 관계가 없다.
컨버전 서비스는 @RequestParam , @ModelAttribute , @PathVariable , 뷰 템플릿 등에서 사용할 수 있다.

json은 jackson dataformat 검색해서 사용!
