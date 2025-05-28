import os
import json
import pandas as pd
import matplotlib.pyplot as plt

# 指定输出目录
output_dir = 'output/recommendation_result'

# 自动查找第一个 JSON 文件
json_file = None
for filename in os.listdir(output_dir):
    if filename.endswith('.json'):
        json_file = os.path.join(output_dir, filename)
        break

if json_file is None:
    raise FileNotFoundError("No JSON file found in output/recommendation_result")

# 加载 JSON 行格式数据
records = []
with open(json_file, 'r', encoding='utf-8') as f:
    for line in f:
        data = json.loads(line)
        uid = data.get('userId')
        for rec in data.get('recommendations', []):
            records.append({
                'userId': uid,
                'itemId': rec.get('itemId'),
                'rating': rec.get('rating')
            })

# 创建 DataFrame 并绘图
df = pd.DataFrame(records)
pivot = df.pivot(index='userId', columns='itemId', values='rating')
pivot.plot(kind='bar', stacked=True)

plt.title('User Recommendation Scores')
plt.ylabel('Rating')
plt.xlabel('UserId')
plt.legend(title='ItemId')
plt.tight_layout()
plt.savefig('output/recommendation_plot.png')
plt.show()
