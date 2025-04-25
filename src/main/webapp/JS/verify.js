function validatePassword(password) {
    const regex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&*])[A-Za-z\d@#$%^&*]{6,18}$/;
    return regex.test(password);
}

const validationConfig = {
    register: {
        fields: {
            username: {
                element: document.getElementById('username'),
                errorElement: document.getElementById('nameError'),
                validate: (value) => {
                    if (value.length < 3) return '用户名需至少3位';
                    if (value.length > 16) return '用户名最多16位';
                    return '';
                }
            },
            email: {
                element: document.getElementById('email'),
                errorElement: document.getElementById('emailError'),
                validate: (value) => {
                    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                    return emailRegex.test(value) ? '' : '请输入有效的电子邮件地址';
                }
            },
            password: {
                element: document.getElementById('password'),
                errorElement: document.getElementById('passwordError'),
                validate: (value) => validatePassword(value) ? '' : '密码需6-18位，包含大小写字母、数字和特殊符号'
            }
        },
        endpoint: '/register',
        successRedirect: '/views/lr.html'
    },
    login: {
        fields: {
            loginEmail: {
                element: document.getElementById('loginEmail'),
                errorElement: document.getElementById('loginEmailError'),
                validate: (value) => {
                    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                    return emailRegex.test(value) ? '' : '请输入有效的电子邮件地址';
                }
            },
            loginPassword: {
                element: document.getElementById('loginPassword'),
                errorElement: document.getElementById('loginPasswordError'),
                validate: (value) => value ? '' : '密码不能为空'
            }
        },
        endpoint: '/login',
        successRedirect: '/views/function.html'
    }
};

async function handleFormSubmit(e, type) {
    e.preventDefault();
    const config = validationConfig[type];
    let isValid = true;

    for (const [fieldName, fieldConfig] of Object.entries(config.fields)) {
        if (!fieldConfig.element) {
            console.error(`[${type}] 字段 ${fieldName} 未找到，检查ID: ${fieldConfig.element?.id || '未定义'}`);
            isValid = false;
            continue;
        }
        const value = fieldConfig.element.value.trim();
        const error = fieldConfig.validate(value);

        if (error) {
            fieldConfig.errorElement.textContent = error;
            fieldConfig.errorElement.style.display = 'block';
            isValid = false;
        } else {
            fieldConfig.errorElement.style.display = 'none';
        }
    }

    if (!isValid) return;

    const formData = new URLSearchParams();
    if (type === 'register') {
        formData.append('username', config.fields.username.element.value);
        formData.append('email', config.fields.email.element.value);
        formData.append('password', config.fields.password.element.value);
    } else {
        formData.append('loginEmail', config.fields.loginEmail.element.value);
        formData.append('loginPassword', config.fields.loginPassword.element.value);
    }

    try {
        const response = await fetch(config.endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        });

        if (response.ok) {
            window.location.href = config.successRedirect;
        } else {
            const errorData = await response.json();
            showGlobalError(errorData.message || `${type === 'register' ? '注册' : '登录'}失败`);
        }
    } catch (error) {
        showGlobalError('网络连接异常，请稍后重试');
    }
}

function showGlobalError(message) {
    const errorElement = document.createElement('div');
    errorElement.className = 'global-error';
    errorElement.textContent = message;
    document.body.appendChild(errorElement);
    setTimeout(() => errorElement.remove(), 3000);
}

function setupRealTimeValidation(fields) {
    Object.values(fields).forEach(config => {
        // 添加安全校验
        if (!config.element || !config.errorElement) {
            console.warn(`Missing elements for field: ${config.element?.id}`);
            return;
        }

        config.element.addEventListener('input', () => {
            const error = config.validate(config.element.value.trim());
            config.errorElement.textContent = error || '';
            config.errorElement.style.display = error ? 'block' : 'none';
        });
    });
}

document.addEventListener('DOMContentLoaded', () => {
    // 初始化实时验证
    if (document.getElementById('register')) {
        setupRealTimeValidation(validationConfig.register.fields);
    }
    if (document.getElementById('login')) {
        setupRealTimeValidation(validationConfig.login.fields);
    }

    // 绑定表单提交事件
    const registerForm = document.getElementById('register');
    if (registerForm) {
        registerForm.addEventListener('submit', (e) => handleFormSubmit(e, 'register'));
    }

    const loginForm = document.getElementById('login');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => handleFormSubmit(e, 'login'));
    }
});