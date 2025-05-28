import os
import pandas as pd
import matplotlib.pyplot as plt

# 自动查找 CSV 文件
output_dir = 'output/regression_result'
csv_file = None
for filename in os.listdir(output_dir):
    if filename.endswith('.csv'):
        csv_file = os.path.join(output_dir, filename)
        break

if csv_file is None:
    raise FileNotFoundError("No CSV file found in output/regression_result")

# 读取数据
df = pd.read_csv(csv_file)

# 绘图
plt.figure(figsize=(8, 6))
plt.scatter(df['x'], df['y'], label='Real', color='blue')
plt.plot(df['x'], df['prediction'], label='Prediction', color='red')
plt.xlabel('x')
plt.ylabel('y')
plt.title('Linear Regression Fit')
plt.legend()
plt.tight_layout()
plt.savefig('output/regression_plot.png')
plt.show()
