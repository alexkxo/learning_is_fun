# Generated by Django 4.1.4 on 2023-01-06 21:36

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('app_one', '0002_album'),
    ]

    operations = [
        migrations.AddField(
            model_name='song',
            name='album',
            field=models.ForeignKey(default='NULL', on_delete=django.db.models.deletion.CASCADE, to='app_one.album'),
        ),
    ]
